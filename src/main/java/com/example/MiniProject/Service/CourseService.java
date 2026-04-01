package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.CourseSummaryDto;
import com.example.MiniProject.Dto.DepartmentSummaryDto;
import com.example.MiniProject.Dto.Requests.CourseRequestDto;
import com.example.MiniProject.Dto.Responses.CourseResponseDto;
import com.example.MiniProject.Entity.BankAccount;
import com.example.MiniProject.Entity.Course;
import com.example.MiniProject.Entity.Currency;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.CourseMapper;
import com.example.MiniProject.Repository.CourseRepo;
import com.example.MiniProject.Repository.CurrencyRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Specification.CourseSpecification;
import com.example.MiniProject.Utils.CgpaHelper;
import com.example.MiniProject.Utils.CurrencyHelper;
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
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepo courseRepo;
    private final CourseMapper courseMapper;
    private final CurrencyRepo currencyRepo;
    public ApiResponse<CourseResponseDto> addCourse(
            String currencyCode,
            CourseRequestDto courseRequestDto
    ) {

        log.info("Attempting to create course with name {}",
                courseRequestDto.getCourseName());

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

            String courseName = courseRequestDto.getCourseName().trim();

            if (courseRepo.existsByCourseNameIgnoreCaseAndTenantId(courseName, tenantId)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Course name already exists",
                        null,
                        LocalDateTime.now()
                );
            }

            String normalizedCurrency = currencyCode.trim().toUpperCase();

            Currency currency = currencyRepo.findByCurrencyCode(normalizedCurrency)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Currency not found with code " + normalizedCurrency
                            )
                    );

            double originalFee = courseRequestDto.getCourseFee();

            double convertedFee = CurrencyHelper.convertToInr(
                    originalFee,
                    normalizedCurrency,
                    currency
            );

            if (courseRequestDto.getDeadLine()
                    .isBefore(LocalDate.now().plusDays(5))) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Deadline must be at least 5 days from today",
                        null,
                        LocalDateTime.now()
                );
            }

            Course course = courseMapper.toEntity(courseRequestDto);

            course.setCourseName(courseName);
            course.setCourseFee(convertedFee);

            Course saved = courseRepo.save(course);

            log.info("Course created successfully with id {} for tenant {}",
                    saved.getCourseId(), tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Course created successfully",
                    courseMapper.toDto(saved),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("failed to add Course");

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Error while creating course: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while creating course",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<List<CourseResponseDto>> getAllCourses() {

        try {
            log.info("Fetching all available Courses");

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

            List<Course> courses = courseRepo.findAllByTenantId(tenantId);

            log.info("Number of Courses available for tenant {}: {}", tenantId, courses.size());

            List<CourseResponseDto> response =
                    courseMapper.toDtoList(courses);

            log.info("Courses fetched successfully");

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Courses fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch Courses", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch Courses",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<CourseResponseDto> getCourseById(Long courseId) {

        try {
            log.info("Fetching Course with id {}", courseId);

            if (courseId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Course id must not be null",
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

            Course course = courseRepo
                    .findByCourseIdAndTenantId(courseId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Course not found with id: " + courseId));

            CourseResponseDto response = courseMapper.toDto(course);

            log.info("Course fetched successfully for tenant {}", tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Course fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Course not found with id {}", courseId);

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch Course", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch Course",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<CourseResponseDto> updateCourse(
            Long courseId,
            String currencyCode,
            CourseRequestDto courseRequestDto
    ) {

        log.info("Attempting to update course with id {}", courseId);

        try {

            if (courseId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Course id must not be null",
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

            Course existingCourse = courseRepo
                    .findByCourseIdAndTenantId(courseId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Course not found with id " + courseId)
                    );

            String normalizedCourseName = courseRequestDto.getCourseName().trim();

            if (!existingCourse.getCourseName().equalsIgnoreCase(normalizedCourseName)
                    && courseRepo.existsByCourseNameIgnoreCaseAndTenantId(normalizedCourseName, tenantId)) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Course name already exists",
                        null,
                        LocalDateTime.now()
                );
            }

            if (currencyCode == null || currencyCode.isBlank()) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Currency code is required",
                        null,
                        LocalDateTime.now()
                );
            }

            String normalizedCurrencyCode = currencyCode.trim().toUpperCase();

            Currency currency = currencyRepo
                    .findByCurrencyCode(normalizedCurrencyCode)
                    .orElseThrow(() ->
                            new RuntimeException("Currency not found: " + normalizedCurrencyCode)
                    );

            double inputFee = courseRequestDto.getCourseFee();

            if (inputFee <= 0) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Course fee must be greater than zero",
                        null,
                        LocalDateTime.now()
                );
            }

            double convertedFee =
                    CurrencyHelper.convertToInr(inputFee, normalizedCurrencyCode, currency);

            if (courseRequestDto.getDeadLine()
                    .isBefore(LocalDate.now().plusDays(5))) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Deadline must be at least 5 days from today",
                        null,
                        LocalDateTime.now()
                );
            }

            if (!existingCourse.getStudents().isEmpty() &&
                    !existingCourse.getCourseFee().equals(convertedFee)) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Course fee cannot be changed after students enroll",
                        null,
                        LocalDateTime.now()
                );
            }

            int currentStudentCount = existingCourse.getStudents().size();

            if (courseRequestDto.getMaxCapacity() < currentStudentCount) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Max capacity cannot be less than enrolled students",
                        null,
                        LocalDateTime.now()
                );
            }

            existingCourse.setCourseName(normalizedCourseName);
            existingCourse.setCourseFee(convertedFee);
            existingCourse.setDeadLine(courseRequestDto.getDeadLine());
            existingCourse.setMaxCapacity(courseRequestDto.getMaxCapacity());

            Course updatedCourse = courseRepo.save(existingCourse);

            log.info("Course updated successfully with id {} for tenant {}",
                    updatedCourse.getCourseId(), tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Course updated successfully",
                    courseMapper.toDto(updatedCourse),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Failed to update course: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Error while updating course: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while updating course",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<Void> deleteCourse(Long courseId) {

        log.info("Attempting to delete course with id {}", courseId);

        try {

            if (courseId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Course id is required",
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

            Course course = courseRepo
                    .findByCourseIdAndTenantId(courseId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Course not found with id " + courseId)
                    );

            if (course.getDeadLine() != null &&
                    LocalDate.now().isAfter(course.getDeadLine())) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Course cannot be deleted after enrollment deadline",
                        null,
                        LocalDateTime.now()
                );
            }

            Set<Student> enrolledStudents = course.getStudents();

            if (enrolledStudents != null && !enrolledStudents.isEmpty()) {

                for (Student student : enrolledStudents) {

                    Float cgpa = student.getCGpa();
                    double baseFee = course.getCourseFee();

                    double refundAmount = 0.0;

                    if (cgpa != null && cgpa >= 6.0) {
                        refundAmount = CgpaHelper.calculateDiscount(baseFee, cgpa);
                    }

                    BankAccount bankAccount = student.getBankAccount();

                    if (bankAccount != null && refundAmount > 0) {

                        double newBalance = bankAccount.getBalance() + refundAmount;

                        bankAccount.setBalance(
                                Math.round(newBalance * 100.0) / 100.0
                        );
                    }

                    student.getCourses().remove(course);
                }
            }

            course.getStudents().clear();

            courseRepo.delete(course);

            log.info("Course deleted successfully with id {} for tenant {}",
                    courseId, tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Course deleted successfully and enrolled students refunded",
                    null,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Course deletion failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while deleting course", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while deleting course",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<PageResponse<CourseSummaryDto>> getAllCoursesWithPagination(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            Long courseId,
            String courseName,
            LocalDate deadLine) {
        try{
            log.info("trying to fetch all courses");
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

            Specification<Course> spec= CourseSpecification.getSpecification(tenantId, courseId, courseName, deadLine);

            Page<Course> pageCourse=courseRepo.findAll(spec,pageable);

            List<CourseSummaryDto> courseSummaryDtos=courseMapper.toSummarayDtoList(pageCourse.getContent());
            PageResponse<CourseSummaryDto> pageResponsedtos=new PageResponse<>(
                    courseSummaryDtos,
                    pageCourse.getNumber(),
                    pageCourse.getSize(),
                    pageCourse.getTotalElements(),
                    pageCourse.getTotalPages(),
                    pageCourse.isLast()
            );
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Courses fetched successfully",
                    pageResponsedtos,
                    LocalDateTime.now()
            );

        }catch(Exception e){
            log.warn("Error while fetching courses");
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<CourseResponseDto>> getAllCoursesWithTenants() {
        try {
            log.info("Fetching all available Courses");

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

            List<Course> courses = courseRepo.findAllByTenantId(tenantId);

            log.info("Number of Courses available for tenant {}: {}", tenantId, courses.size());

            List<CourseResponseDto> response =
                    courseMapper.toDtoList(courses);

            log.info("Courses fetched successfully for tenant {}", tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Courses fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch Courses", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch Courses",
                    null,
                    LocalDateTime.now()
            );
        }
    }
}
