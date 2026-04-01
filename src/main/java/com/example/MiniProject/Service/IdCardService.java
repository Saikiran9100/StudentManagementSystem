package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.IdCardSummaryDto;
import com.example.MiniProject.Dto.Requests.IdCardRequestDto;
import com.example.MiniProject.Dto.Responses.IdCardResponseDto;
import com.example.MiniProject.Entity.IdCard;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.IdCardMapper;
import com.example.MiniProject.Repository.IdCardRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Specification.IdCardSpecification;
import com.example.MiniProject.Utils.PaginationUtil;
import com.example.MiniProject.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
@Service
@Slf4j
@RequiredArgsConstructor
public class IdCardService {


    private final IdCardRepo idCardRepo;

    private final StudentRepo studentRepo;

    private final IdCardMapper idCardMapper;
    public ApiResponse<IdCardResponseDto> addIdCard(
            Long studentId,
            IdCardRequestDto dto) {

        try {

            if (studentId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student id is required",
                        null,
                        LocalDateTime.now()
                );
            }

            String tenantId = TenantContext.getTenant();

            log.info("Issuing IdCard for student {} under tenant {}", studentId, tenantId);

            Student student = studentRepo
                    .findByStudIdAndTenantId(studentId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found for this tenant"));

            if (!student.isActive()) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Cannot issue IdCard to inactive student",
                        null,
                        LocalDateTime.now()
                );
            }

            if (student.getIdCard() != null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student already has an IdCard",
                        null,
                        LocalDateTime.now()
                );
            }

            String cardNumber = dto.getCardNumber().trim().toUpperCase();

            if (idCardRepo.existsByCardNumberAndTenantId(cardNumber, tenantId)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Card number already exists for this tenant",
                        null,
                        LocalDateTime.now()
                );
            }

            if (dto.getIssueDate().isBefore(student.getAdmissionDate())) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Issue date cannot be before admission date",
                        null,
                        LocalDateTime.now()
                );
            }

            if (dto.getIssueDate().isAfter(LocalDate.now())) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Issue date cannot be in the future",
                        null,
                        LocalDateTime.now()
                );
            }

            LocalDate expiryDate = student.getGraduationDate();

            IdCard idCard = idCardMapper.toEntity(dto);

            idCard.setCardNumber(cardNumber);
            idCard.setExpiryDate(expiryDate);
            idCard.setActive(true);
            idCard.setStudent(student);

            student.setIdCard(idCard);

            IdCard saved = idCardRepo.save(idCard);

            log.info("IdCard issued successfully with id {}", saved.getId());

            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "IdCard created successfully",
                    idCardMapper.toDto(saved),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("IdCard creation failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while creating IdCard", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while creating IdCard",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<List<IdCardResponseDto>> getAllIdCards() {

        try {
            log.info("Trying to fetch all IdCards");

            String tenantId = TenantContext.getTenant();

            if (tenantId == null || tenantId.isBlank()) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Tenant header is missing",
                        null,
                        LocalDateTime.now()
                );
            }

            List<IdCard> idCards = idCardRepo.findAllByTenantId(tenantId);

            for (IdCard idCard : idCards) {

                if (idCard.getExpiryDate() != null &&
                        idCard.getExpiryDate().isBefore(LocalDate.now())) {

                    idCard.setActive(false);
                    idCardRepo.save(idCard);
                }
            }

            log.info("Number of IdCards available for tenant {}: {}", tenantId, idCards.size());

            List<IdCardResponseDto> response =
                    idCardMapper.toDtoList(idCards);

            log.info("IdCards fetched successfully");

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "IdCards fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch IdCards", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch IdCards",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<IdCardResponseDto> getIdCardById(Long id) {

        try {

            log.info("Trying to fetch IdCard using id {}", id);

            if (id == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "IdCard id must not be null",
                        null,
                        LocalDateTime.now()
                );
            }

            String tenantId = TenantContext.getTenant();

            IdCard idCard = idCardRepo
                    .findByIdAndTenantId(id, tenantId)
                    .orElseThrow(()->
                            new RuntimeException("IdCard not found for this tenant"));
            IdCardResponseDto response = idCardMapper.toDto(idCard);

            log.info("IdCard fetched successfully for tenant {}", tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "IdCard fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("IdCard not found with id {}", id);

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while fetching IdCard", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch IdCard",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<IdCardResponseDto> updateIdCard(
            Long id,
            IdCardRequestDto dto) {

        try {

            if (id == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "IdCard id must not be null",
                        null,
                        LocalDateTime.now()
                );
            }

            String tenantId = TenantContext.getTenant();

            IdCard existing = idCardRepo
                    .findByIdAndTenantId(id, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("IdCard not found for this tenant"));

            Student student = existing.getStudent();

            if (existing.getExpiryDate() != null &&
                    existing.getExpiryDate().isBefore(LocalDate.now())) {

                existing.setActive(false);

                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Expired IdCard cannot be modified",
                        null,
                        LocalDateTime.now()
                );
            }

            if (dto.getIssueDate() == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Issue date is required",
                        null,
                        LocalDateTime.now()
                );
            }

            if (dto.getIssueDate().isBefore(student.getAdmissionDate())) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Issue date cannot be before student admission date",
                        null,
                        LocalDateTime.now()
                );
            }

            if (dto.getIssueDate().isAfter(LocalDate.now())) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Issue date cannot be after today",
                        null,
                        LocalDateTime.now()
                );
            }

            existing.setStandard(dto.getStandard());
            existing.setSection(dto.getSection());
            existing.setAddress(dto.getAddress());
            existing.setIssueDate(dto.getIssueDate());
            existing.setExpiryDate(student.getGraduationDate());

            IdCard saved = idCardRepo.save(existing);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "IdCard updated successfully",
                    idCardMapper.toDto(saved),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while updating IdCard",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<Void> deleteIdCard(Long idCardId) {

        log.info("Attempting to delete id card with id {}", idCardId);

        try {

            if (idCardId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "IdCard id is required",
                        null,
                        LocalDateTime.now()
                );
            }

            String tenantId = TenantContext.getTenant();

            IdCard idCard = idCardRepo
                    .findByIdAndTenantId(idCardId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("IdCard not found for this tenant")
                    );

            Student student = idCard.getStudent();

            if (student == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Associated student not found for this IdCard",
                        null,
                        LocalDateTime.now()
                );
            }

            student.setIdCard(null);
            studentRepo.save(student);

            idCardRepo.delete(idCard);

            log.info("IdCard deleted successfully with id {}", idCardId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "IdCard deleted successfully",
                    null,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("IdCard deletion failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while deleting IdCard", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while deleting IdCard",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<PageResponse<IdCardSummaryDto>> getAllIdCardsWithPagination(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            Long id,
            String cardNumber,
            Boolean active,
            LocalDate issueDate,
            LocalDate expiryDate,
            Long studentId) {

        try {
            String tenantId = TenantContext.getTenant();

            if (tenantId == null || tenantId.isBlank()) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Tenant header is missing",
                        null,
                        LocalDateTime.now()
                );
            }

            Pageable pageable = PaginationUtil.createPageable(
                    pageNo,
                    pageSize,
                    sortBy,
                    sortDir
            );

            Specification<IdCard> spec =
                    IdCardSpecification.getSpecification(
                            tenantId, id, cardNumber, active, issueDate, expiryDate, studentId
                    );

            Page<IdCard> page = idCardRepo.findAll(spec, pageable);

            List<IdCardSummaryDto> dtoList =
                    idCardMapper.toSummaryDtoList(page.getContent());

            PageResponse<IdCardSummaryDto> pageResponse =
                    new PageResponse<>(
                            dtoList,
                            page.getNumber(),
                            page.getSize(),
                            page.getTotalElements(),
                            page.getTotalPages(),
                            page.isLast()
                    );

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "IdCards fetched successfully",
                    pageResponse,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch IdCards",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<IdCardResponseDto>> getAllIdCardsWithTenants() {
        try {

            String tenantId = TenantContext.getTenant();

            log.info("Fetching IdCards for tenant {}", tenantId);

            List<IdCard> idCards = idCardRepo.findAllByTenantId(tenantId);

            LocalDate today = LocalDate.now();

            for (IdCard idCard : idCards) {

                if (idCard.getExpiryDate() != null &&
                        idCard.getExpiryDate().isBefore(today) &&
                        idCard.isActive()) {

                    idCard.setActive(false);
                }
            }

            idCardRepo.saveAll(idCards);

            log.info("Number of IdCards available for tenant {} : {}", tenantId, idCards.size());

            List<IdCardResponseDto> response =
                    idCardMapper.toDtoList(idCards);

            log.info("IdCards fetched successfully");

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "IdCards fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch IdCards", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch IdCards",
                    null,
                    LocalDateTime.now()
            );
        }
    }
}
