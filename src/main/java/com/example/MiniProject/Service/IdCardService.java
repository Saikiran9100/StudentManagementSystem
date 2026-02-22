package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.Requests.IdCardRequestDto;
import com.example.MiniProject.Dto.Responses.IdCardResponseDto;
import com.example.MiniProject.Entity.IdCard;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.IdCardMapper;
import com.example.MiniProject.Repository.IdCardRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
@Service
@Slf4j
@RequiredArgsConstructor
public class IdCardService {


    private final IdCardRepo idCardRepo;

   private final StudentRepo studentRepo;

    private final IdCardMapper idCardMapper;

    public ApiResponse<IdCardResponseDto> addIdCard(Long studId,IdCardRequestDto idCardRequestDto) {
        try {
            log.info("Attemting to add idcard for student");
            if(studId==null){
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student id is Required to add id card",
                        null,
                        LocalDateTime.now()
                );
            }
            Student student = studentRepo.findById(studId)
                    .orElseThrow(() -> new RuntimeException("Student not found with studid:"));

            if (student.getIdCard() != null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Student already assiged an idcard",
                        null,
                        LocalDateTime.now()
                );
            }
            IdCard idcard = idCardMapper.toEntity(idCardRequestDto);

            idcard.setStudent(student);
            student.setIdCard(idcard);
            Student savedstudent = studentRepo.save(student);
            IdCardResponseDto response = idCardMapper.toDto(savedstudent.getIdCard());
            log.info("idcard added successfully");
            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Successfully idcard Added to Student ",
                    response,
                    LocalDateTime.now()
            );
        }catch (RuntimeException e){
            log.warn("failed to add idcard to student");
            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
           log.warn("failed to add idcard to student");
           return new ApiResponse<>(
                   false,
                   HttpStatus.INTERNAL_SERVER_ERROR,
                   e.getMessage(),
                   null,
                   LocalDateTime.now()
           );
        }
    }

    public ApiResponse<List<IdCardResponseDto>> getAllIdCards() {
        try {
            log.info("trying to fetch id card details");
            List<IdCard> idCardList = idCardRepo.findAll();
            List<IdCardResponseDto> response= idCardMapper.toDtoList(idCardList);
            log.info("total idcards fetched {}",response.size());
            return new ApiResponse<List<IdCardResponseDto>>(
                    true,
                    HttpStatus.OK,
                    "Idcards fetched successfully",
                    response,
                    LocalDateTime.now()
            );
        }catch (RuntimeException e){
            log.warn("failed to fetch idcards");
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()

            );
        }
    }

    public ApiResponse<IdCardResponseDto> getIdCardById(Long id) {
        try {
            log.info("trying to fetch Idcard usind id {}", id);
            IdCard idcard = idCardRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("idcard not found"));

            IdCardResponseDto response = idCardMapper.toDto(idcard);
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Idcard fetched succesfully",
                    response,
                    LocalDateTime.now()
            );
        } catch (RuntimeException e) {
            log.warn("failed to load idcard");
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
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<IdCardResponseDto> updateIdCard(
            Long studId,
            Long id,
            IdCardRequestDto idCardRequestDto) {

        log.info("Updating IdCard with id {}", id);

        try {

            if (studId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "StudentId must be provided",
                        null,
                        LocalDateTime.now()
                );
            }

            Student student = studentRepo.findById(studId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found with id " + studId));

            IdCard existingCard = idCardRepo.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("IdCard not found with id " + id));

            existingCard.setStandard(idCardRequestDto.getStandard());
            existingCard.setSection(idCardRequestDto.getSection());
            existingCard.setAddress(idCardRequestDto.getAddress());
            existingCard.setStudent(student);

            IdCard updatedCard = idCardRepo.save(existingCard);

            IdCardResponseDto response = idCardMapper.toDto(updatedCard);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "IdCard updated successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Update failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while updating IdCard", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }


    public ApiResponse<Void>  deleteIdCardById(Long id) {
        try{
            log.info("Attemptimg to delete studnet with id {}",id);

            IdCard exist=idCardRepo.findById(id)
                    .orElseThrow(()-> new RuntimeException("Idcard not existed"));
            Student student=exist.getStudent();
            if(student!=null){
                student.setIdCard(null);
                studentRepo.save(student);
            }
            idCardRepo.deleteById(id);
            log.info("idcard deleted successfully");
            return new ApiResponse<>(
                true,
                HttpStatus.OK,
                "idcard deleted Successfully",
                null,
                LocalDateTime.now()
        );
        }
    catch(RuntimeException e){
        log.warn("Idcard with id is not found");
        return new ApiResponse<>(
                false,
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                null,
                LocalDateTime.now()
        );
    }catch(Exception e){
            log.warn("error while deletng student");
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
}
}
