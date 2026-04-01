package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.BankAccountSummaryDto;
import com.example.MiniProject.Dto.Requests.BankAccountRequestDto;
import com.example.MiniProject.Dto.Responses.BankAccountResponseDto;
import com.example.MiniProject.Dto.StudentSummaryDto;
import com.example.MiniProject.Entity.BankAccount;
import com.example.MiniProject.Entity.Currency;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.BankAccountMapper;
import com.example.MiniProject.Repository.BankAccountRepo;
import com.example.MiniProject.Repository.CurrencyRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Specification.BankAccountSpecification;
import com.example.MiniProject.Specification.StudentSpecification;
import com.example.MiniProject.Utils.PaginationUtil;
import com.example.MiniProject.config.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.MiniProject.Utils.CurrencyHelper;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BankAccountService {
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepo bankAccountRepo;
    private final StudentRepo studentRepo;
    private final CurrencyRepo currencyRepo;

    public ApiResponse<BankAccountResponseDto> addBankAccount(
            Long studentId,
            String currencyCode,
            BankAccountRequestDto dto) {

        log.info("Attempting to create bank account for student id {}", studentId);

        try {

            String tenantId = TenantContext.getTenant();

            Student student = studentRepo.findByStudIdAndTenantId(studentId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found with id " + studentId));

            if (student.getBankAccount() != null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Student already has a bank account",
                        null,
                        LocalDateTime.now()
                );
            }

            String accountNo = dto.getAccountNo().trim();

            if (bankAccountRepo.existsByAccountNoAndTenantId(accountNo, tenantId)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Account number already exists",
                        null,
                        LocalDateTime.now()
                );
            }

            String normalizedCurrency = currencyCode.trim().toUpperCase();

            Currency currency = currencyRepo.findByCurrencyCode(normalizedCurrency)
                    .orElseThrow(() ->
                            new RuntimeException("Currency not found with code " + normalizedCurrency));

            double originalBalance = dto.getBalance();

            if (originalBalance < 0) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Balance cannot be negative",
                        null,
                        LocalDateTime.now()
                );
            }

            double convertedBalance = CurrencyHelper.convertToInr(
                    originalBalance,
                    normalizedCurrency,
                    currency
            );

            dto.setBalance(convertedBalance);

            BankAccount bankAccount = bankAccountMapper.toEntity(dto);
            bankAccount.setAccountNo(accountNo);
            bankAccount.setStudent(student);

            BankAccount saved = bankAccountRepo.save(bankAccount);

            student.setBankAccount(saved);

            log.info("Bank account created successfully for student id {}", studentId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Bank account created successfully",
                    bankAccountMapper.toDto(saved),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Failed to create bank account: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while creating bank account", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<BankAccountResponseDto>> getAllBankAccounts() {

        try {
            log.info("Fetching all available BankAccounts");

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

            List<BankAccount> bankAccounts =
                    bankAccountRepo.findAllByTenantId(tenantId);

            log.info("Number of BankAccounts available for tenant {}: {}",
                    tenantId, bankAccounts.size());

            List<BankAccountResponseDto> response =
                    bankAccountMapper.toDtoList(bankAccounts);

            log.info("BankAccounts fetched successfully");

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "BankAccounts fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch BankAccounts", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch BankAccounts",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<BankAccountResponseDto> getBankAccountById(Long bankId) {

        try {
            log.info("Fetching BankAccount with id {}", bankId);

            if (bankId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "BankAccount id must not be null",
                        null,
                        LocalDateTime.now()
                );
            }

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

            BankAccount bankAccount = bankAccountRepo
                    .findByBankIdAndTenantId(bankId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("BankAccount not found with id: " + bankId));

            BankAccountResponseDto response =
                    bankAccountMapper.toDto(bankAccount);

            log.info("BankAccount fetched successfully for tenant {}", tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "BankAccount fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("BankAccount not found with id {}", bankId);

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch BankAccount", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch BankAccount",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<BankAccountResponseDto> updateBankAccount(
            Long bankId,
            String currencyCode,
            BankAccountRequestDto dto) {

        try {

            log.info("Trying to update BankAccount with id {}", bankId);

            if (bankId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "BankAccount id must not be null",
                        null,
                        LocalDateTime.now()
                );
            }

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

            BankAccount existingAccount = bankAccountRepo
                    .findByBankIdAndTenantId(bankId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("BankAccount not found with id " + bankId));

            String accountNo = dto.getAccountNo().trim();

            if (!existingAccount.getAccountNo().equals(accountNo)
                    && bankAccountRepo.existsByAccountNoAndTenantId(accountNo, tenantId)) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Account number already exists",
                        null,
                        LocalDateTime.now()
                );
            }

            String normalizedCurrency = currencyCode.trim().toUpperCase();

            Currency currency = currencyRepo.findByCurrencyCode(normalizedCurrency)
                    .orElseThrow(() ->
                            new RuntimeException("Currency not found with code " + normalizedCurrency));

            double originalBalance = dto.getBalance();

            if (originalBalance < 0) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Balance cannot be negative",
                        null,
                        LocalDateTime.now()
                );
            }

            double convertedBalance = CurrencyHelper.convertToInr(
                    originalBalance,
                    normalizedCurrency,
                    currency
            );

            dto.setBalance(convertedBalance);

            bankAccountMapper.updateEntityFromDto(dto, existingAccount);

            existingAccount.setAccountNo(accountNo);

            BankAccount saved = bankAccountRepo.save(existingAccount);

            log.info("BankAccount updated successfully with id {} for tenant {}", bankId, tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "BankAccount updated successfully",
                    bankAccountMapper.toDto(saved),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Failed to update BankAccount: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while updating BankAccount", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while updating BankAccount",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<Void> deleteBankAccount(Long bankId) {

        log.info("Attempting to delete bank account with id {}", bankId);

        try {

            if (bankId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Bank account id is required",
                        null,
                        LocalDateTime.now()
                );
            }

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

            BankAccount bankAccount = bankAccountRepo
                    .findByBankIdAndTenantId(bankId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Bank account not found with id " + bankId)
                    );

            Student student = bankAccount.getStudent();

            if (student == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Associated student not found for this bank account",
                        null,
                        LocalDateTime.now()
                );
            }

            student.setBankAccount(null);
            studentRepo.save(student);

            log.info("Bank account deleted successfully with id {} for tenant {}", bankId, tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Bank account deleted successfully",
                    null,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Bank account deletion failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while deleting bank account", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while deleting bank account",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<PageResponse<BankAccountSummaryDto>> getBankAccountsWithPagination(int pageNo, int pageSize, String sortBy, String sortDir, String bankName, String branch) {

        try {
            log.info("Fetching BankAccounts with pagination");
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

            Specification<BankAccount> spec= BankAccountSpecification.getSpecification(tenantId, bankName, branch);

            Page<BankAccount> bankPage = bankAccountRepo.findAll(spec,pageable);

            List<BankAccountSummaryDto> bankDtos =
                    bankAccountMapper.toSummaryDtoList(bankPage.getContent());

            PageResponse<BankAccountSummaryDto> pageResponse =
                    new PageResponse<>(
                            bankDtos,
                            bankPage.getNumber(),
                            bankPage.getSize(),
                            bankPage.getTotalElements(),
                            bankPage.getTotalPages(),
                            bankPage.isLast()
                    );

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "BankAccounts fetched successfully",
                    pageResponse,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("Error while fetching BankAccounts", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to BankAccounts",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<BankAccountResponseDto>> getAllBankAccountsWithTenants() {
        try {
            log.info("Fetching all available BankAccounts");

            String tenantId = TenantContext.getTenant();

            List<BankAccount> bankAccounts =
                    bankAccountRepo.findAllByTenantId(tenantId);

            log.info("Number of BankAccounts available for tenant {}: {}",
                    tenantId, bankAccounts.size());

            List<BankAccountResponseDto> response =
                    bankAccountMapper.toDtoList(bankAccounts);

            log.info("BankAccounts fetched successfully for tenant {}", tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "BankAccounts fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch BankAccounts", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch BankAccounts",
                    null,
                    LocalDateTime.now()
            );
        }
    }
}
