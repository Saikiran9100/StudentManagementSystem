package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.Requests.DepartmentRequestDto;
import com.example.MiniProject.Dto.Responses.DepartmentResponseDto;
import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.DepartmentMapper;
import com.example.MiniProject.Repository.DepartmentRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Service
public class DepartmentService {


    private final DepartmentRepo departmentRepo;

    private final DepartmentMapper departmentMapper;

    private final StudentRepo studentRepo;

    public ApiResponse<DepartmentResponseDto> addDepartment(DepartmentRequestDto departmentRequestDto){

        log.info("Attempting to add department");

        try {

            String deptName = departmentRequestDto.getDeptName();

            if (deptName == null || deptName.trim().isBlank()) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Department name is required",
                        null,
                        LocalDateTime.now()
                );
            }

            deptName = deptName.trim();

            if (departmentRepo.existsByDeptName(deptName)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Department with same name already exists",
                        null,
                        LocalDateTime.now()
                );
            }

            Department department = new Department();
            department.setDeptName(deptName);

            Department saved = departmentRepo.save(department);

            DepartmentResponseDto response = departmentMapper.toDto(saved);

            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Department saved successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            return new ApiResponse<>(
                    false,
                    HttpStatus.CONFLICT,
                    "Duplicate department name",
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e){

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<DepartmentResponseDto>> getAllDepartments(){
        try{
            log.info("fetching all availbale departments");
            List<Department> departments=departmentRepo.findAll();
            log.info("no of departments Availble {}",departments.size());
            List<DepartmentResponseDto> response=departmentMapper.toDtoList(departments);
            log.info("departments fetched Successfully");
            return new ApiResponse<List<DepartmentResponseDto>>(
                    true,
                    HttpStatus.OK,
                    "departments Fetched Successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
          log.warn("failed to fetch students");
          return new ApiResponse<>(
                  false,
                  HttpStatus.INTERNAL_SERVER_ERROR,
                  e.getMessage(),
                  null,
                  LocalDateTime.now()
          );
        }
    }

    public ApiResponse<DepartmentResponseDto> getDepartmentById(Long deptId) {
        try {
            log.info("Trying toi Fetch Department using deptId");

            Department existed = departmentRepo.findById(deptId)
                    .orElseThrow(() -> new RuntimeException("Department with Id id not existed "));

            DepartmentResponseDto response = departmentMapper.toDto(existed);
            log.info("Department Feteched Successfully");
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Department fetched successfully",
                    response,
                    LocalDateTime.now()
            );
        } catch (RuntimeException e) {
            log.warn("failed to load Department");
            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
        catch (Exception e){
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<Void> deleteDepartment(Long deptId){
        try {
            Department exist =departmentRepo.findById(deptId)
                    .orElseThrow(()->new RuntimeException("Depertment with this id is not existed"));
            List<Student> students = studentRepo.findByDepartment_DeptId(deptId);

            for (Student student : students) {
                student.setDepartment(null);
            }

            studentRepo.saveAll(students);

            departmentRepo.deleteById(deptId);
             log.info("Deleting department");
             departmentRepo.deleteById(deptId);
             log.info("Department Deleted Successfully");
             return new ApiResponse<>(
                     true,
                     HttpStatus.OK,
                     "Department Deleted Successfully",
                     null,
                     LocalDateTime.now()
             );



        }catch (RuntimeException e){
            log.warn("department id is not found");
            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()

            );
        }catch (Exception e){
            log.warn("failed to delete department");
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<DepartmentResponseDto> updateDepartment(Long deptId,DepartmentRequestDto departmentRequestDto) {
        try {
            log.info("Attempting to update Department");
            Department existsdept = departmentRepo.findById(deptId)
                    .orElseThrow(() -> new RuntimeException("Department not exists with following id"));
            existsdept.setDeptName(departmentRequestDto.getDeptName());
            Department department=departmentRepo.save(existsdept);
            DepartmentResponseDto response=departmentMapper.toDto(department);
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Department Updated Successfully",
                    response,
                    LocalDateTime.now()
            );
        }catch(RuntimeException e){
            log.warn("Department id is not existed in database");
            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            log.info("Failed to delete Department");
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
