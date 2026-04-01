package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.DepartmentSummaryDto;
import com.example.MiniProject.Dto.Requests.DepartmentRequestDto;
import com.example.MiniProject.Dto.Responses.DepartmentResponseDto;
import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.DepartmentMapper;
import com.example.MiniProject.Repository.DepartmentRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Specification.DepartmentSpecification;
import com.example.MiniProject.Utils.PaginationUtil;
import com.example.MiniProject.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public ApiResponse<DepartmentResponseDto> addDepartment(
            DepartmentRequestDto dto) {

        log.info("Attempting to create department with code {}", dto.getDeptCode());

        try {

            String tenantId = TenantContext.getTenant();

            String deptName = dto.getDeptName().trim();
            String deptCode = dto.getDeptCode().trim().toUpperCase();

            if (departmentRepo.existsByDeptNameIgnoreCaseAndTenantId(deptName, tenantId)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Department name already exists for this tenant",
                        null,
                        LocalDateTime.now()
                );
            }

            if (departmentRepo.existsByDeptCodeAndTenantId(deptCode, tenantId)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Department code already exists for this tenant",
                        null,
                        LocalDateTime.now()
                );
            }

            if (dto.getCapacity() < 5) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Minimum department capacity must be at least 5",
                        null,
                        LocalDateTime.now()
                );
            }

            Department department = departmentMapper.toEntity(dto);

            department.setDeptName(deptName);
            department.setDeptCode(deptCode);
            Department saved = departmentRepo.save(department);

            log.info("Department created successfully with id {}", saved.getDeptId());

            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Department created successfully",
                    departmentMapper.toDto(saved),
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Error while creating department: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while creating department",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<List<DepartmentResponseDto>> getAllDepartments(){
        try{
            log.info("fetching all availbale departments");
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

            List<Department> departments=departmentRepo.findAllByTenantId(tenantId);
            log.info("no of departments available for tenant {} : {}", tenantId, departments.size());
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

            if (deptId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Department id must not be null",
                        null,
                        LocalDateTime.now()
                );
            }

            String tenantId = TenantContext.getTenant();

            log.info("Fetching department {} for tenant {}", deptId, tenantId);

            Department department = departmentRepo
                    .findByDeptIdAndTenantId(deptId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Department not found"));

            DepartmentResponseDto response =
                    departmentMapper.toDto(department);

            log.info("Department fetched successfully");

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Department fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Department not found with id {}", deptId);

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while fetching department", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while fetching department",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<Void> deleteDepartment(Long deptId) {

        log.info("Attempting to delete department with id {}", deptId);

        try {

            if (deptId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Department id is required",
                        null,
                        LocalDateTime.now()
                );
            }

            String tenantId = TenantContext.getTenant();

            Department department = departmentRepo
                    .findByDeptIdAndTenantId(deptId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Department not found")
                    );

            List<Student> students = department.getStudents();

            if (students != null && !students.isEmpty()) {

                for (Student student : students) {
                    student.setDepartment(null);
                }

                studentRepo.saveAll(students);
            }

            departmentRepo.delete(department);

            log.info("Department deleted successfully with id {} for tenant {}", deptId, tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Department deleted successfully",
                    null,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Department deletion failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while deleting department", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while deleting department",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<DepartmentResponseDto> updateDepartment(
            Long deptId,
            DepartmentRequestDto dto) {

        try {

            log.info("Attempting to update Department with id {}", deptId);

            String tenantId = TenantContext.getTenant();

            Department existingDept = departmentRepo
                    .findByDeptIdAndTenantId(deptId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Department not found"));

            String newDeptName = dto.getDeptName().trim();
            String newDeptCode = dto.getDeptCode().trim().toUpperCase();
            if (!existingDept.getDeptName().equalsIgnoreCase(newDeptName)
                    && departmentRepo.existsByDeptNameIgnoreCaseAndTenantId(newDeptName, tenantId)) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Department name already exists for this tenant",
                        null,
                        LocalDateTime.now()
                );
            }

            if (!existingDept.getDeptCode().equals(newDeptCode)
                    && departmentRepo.existsByDeptCodeAndTenantId(newDeptCode, tenantId)) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Department code already exists for this tenant",
                        null,
                        LocalDateTime.now()
                );
            }

            int currentStudentCount = existingDept.getCurrentStrength() == null
                    ? 0
                    : existingDept.getStudents().size();

            if (dto.getCapacity() < currentStudentCount) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Capacity cannot be less than current student count",
                        null,
                        LocalDateTime.now()
                );
            }

            existingDept.setDeptName(newDeptName);
            existingDept.setDeptCode(newDeptCode);
            existingDept.setCapacity(dto.getCapacity());

            Department updated = departmentRepo.save(existingDept);

            log.info("Department updated successfully with id {}", updated.getDeptId());

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Department updated successfully",
                    departmentMapper.toDto(updated),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Department update failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while updating Department", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while updating department",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<PageResponse<DepartmentSummaryDto>> getAllDepartmentsWithPagination(int pageNo, int pageSize, String sortBy, String sortDir, Long deptId,String deptCode, String deptName) {
        try{
            log.info("trying to fetch departmnets");
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

            Specification<Department> spec= DepartmentSpecification.getSpecification(tenantId, deptId, deptCode, deptName);

            Page<Department> departmentPage=departmentRepo.findAll(spec,pageable);

            List<DepartmentSummaryDto> dtos=departmentMapper.toSummaryDtoList(departmentPage.getContent());
            PageResponse<DepartmentSummaryDto> pageResponse=
                    new PageResponse<>(
                            dtos,
                            departmentPage.getNumber(),
                            departmentPage.getSize(),
                            departmentPage.getTotalElements(),
                            departmentPage.getTotalPages(),
                            departmentPage.isLast()
                    );
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Departments fetched successfully",
                    pageResponse,
                    LocalDateTime.now()
            );
        }catch(Exception e){
            log.warn("failed to load Departments");
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<DepartmentResponseDto>> getAllDepartmentsWithTenants() {

        try {

            String tenantId = TenantContext.getTenant();

            log.info("Fetching all departments for tenant {}", tenantId);

            List<Department> departments = departmentRepo.findAllByTenantId(tenantId);

            log.info("Number of departments available for tenant {} : {}", tenantId, departments.size());

            List<DepartmentResponseDto> response = departmentMapper.toDtoList(departments);

            log.info("Departments fetched successfully");

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Departments fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch departments: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch departments",
                    null,
                    LocalDateTime.now()
            );
        }
    }
}
