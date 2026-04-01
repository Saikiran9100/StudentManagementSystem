package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.Requests.StudentRequestDto;
import com.example.MiniProject.Dto.Responses.StudentResponseDto;
import com.example.MiniProject.Dto.StudentSummaryDto;
import com.example.MiniProject.Entity.*;
import com.example.MiniProject.Mapper.StudentMapper;
import com.example.MiniProject.Repository.CourseRepo;
import com.example.MiniProject.Repository.DepartmentRepo;
import com.example.MiniProject.Repository.IdCardRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Specification.StudentSpecification;
import com.example.MiniProject.Utils.CgpaHelper;
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

@RequiredArgsConstructor
@Slf4j
@Service
public class StudentService {


   private final StudentRepo studentRepo;

    private final DepartmentRepo departmentRepo;
    private final StudentMapper studentMapper;
    private final CourseRepo courseRepo;
    private final IdCardRepo idCardRepo;
    private final int MAX_ALLOWED_COURSES = 5;

    public ApiResponse<StudentResponseDto> addStudent(StudentRequestDto studentRequestDto) {

        log.info("Trying to add student");

        try {
            String tenantId = TenantContext.getTenant();

            if(studentRepo.existsByEmailAndTenantId(
                    studentRequestDto.getEmail(),
                    tenantId
            )){
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Student with this email already exists in this tenant",
                        null,
                        LocalDateTime.now()
                );
            }
            if(studentRequestDto.getAdmissionDate()==null){
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Admission date is Required",
                        null,
                        LocalDateTime.now()
                );
            }
            if(studentRequestDto.getAdmissionDate().isAfter(LocalDate.now())){
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Admission date cannot be in Future",
                        null,
                        LocalDateTime.now()
                );
            }
            Student student = studentMapper.toEntity(studentRequestDto);
            LocalDate graduationdate=studentRequestDto.getAdmissionDate().plusYears(4);

            student.setGraduationDate(graduationdate);
            student.setActive(true);
             Student savedStudent = studentRepo.save(student);

            StudentResponseDto response = studentMapper.toDto(savedStudent);

            log.info("Student added successfully with id {}", savedStudent.getStudId());

            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Student added successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while adding student", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<PageResponse<StudentSummaryDto>> getAllStudentsWithPagination(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            String email,
            Float cGpa) {

        try {
            log.info("Fetching students with pagination");
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
            Specification<Student> spec= StudentSpecification.getSpecification(tenantId, email, cGpa);

            Page<Student> studentPage = studentRepo.findAll(spec,pageable);

            List<StudentSummaryDto> studentDtos =
                    studentMapper.toSummaryDtoList(studentPage.getContent());

            PageResponse<StudentSummaryDto> pageResponse =
                    new PageResponse<>(
                            studentDtos,
                            studentPage.getNumber(),
                            studentPage.getSize(),
                            studentPage.getTotalElements(),
                            studentPage.getTotalPages(),
                            studentPage.isLast()
                    );

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Students fetched successfully",
                    pageResponse,
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            log.error("Error while fetching students", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch students",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<StudentResponseDto> getStudentById(Long studId) {

        try {
            log.info("Fetching student with id: {}", studId);
            if (studId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student id must not be null",
                        null,
                        LocalDateTime.now()
                );
            }

            String tenantId = TenantContext.getTenant();

            Student student = studentRepo
                    .findByStudIdAndTenantId(studId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found with id in the tenant: {}" +tenantId ));
            StudentResponseDto dto = studentMapper.toDto(student);

            log.info("Student fetched successfully");

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Student fetched successfully",
                    dto,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Student not found with id in this tenant{}", studId);

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }catch(Exception e){
            log.warn("failed to load student detail");
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<Void> deleteStudent(Long studId) {

        log.info("Attempting to delete student with id {}", studId);

        try {

            if (studId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student id is required",
                        null,
                        LocalDateTime.now()
                );
            }

            String tenantId = TenantContext.getTenant();

            Student student = studentRepo
                    .findByStudIdAndTenantId(studId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found with id " + studId)
                    );

            if (student.getCourses() != null && !student.getCourses().isEmpty()) {

                for (Course course : student.getCourses()) {
                    course.getStudents().remove(student);
                }

                student.getCourses().clear();
            }

            Department department = student.getDepartment();

            if (department != null) {
                department.setCurrentStrength(department.getCurrentStrength() - 1);
            }

            student.setDepartment(null);

            studentRepo.delete(student);

            log.info("Student deleted successfully with id {}", studId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Student deleted successfully",
                    null,
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Student deletion failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while deleting student", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while deleting student",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<StudentResponseDto> assignDepartment(
            Long studId,
            Long deptId) {

        log.info("Assigning department {} to student {}", deptId, studId);

        try {

            if (studId == null || deptId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "StudentId and DepartmentId are required",
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

            Student student = studentRepo
                    .findByStudIdAndTenantId(studId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found with id " + studId)
                    );

            Department department = departmentRepo
                    .findByDeptIdAndTenantId(deptId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Department not found with id " + deptId)
                    );

            if (student.getDepartment() != null &&
                    student.getDepartment().getDeptId().equals(deptId)) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Student is already assigned to this department",
                        null,
                        LocalDateTime.now()
                );
            }

            if (student.getLastDepartmentChangeDate() != null &&
                    student.getLastDepartmentChangeDate()
                            .isAfter(LocalDate.now().minusYears(1))) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Department change allowed only once per year",
                        null,
                        LocalDateTime.now()
                );
            }

            if (department.getCurrentStrength() >= department.getCapacity()) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Department capacity is full",
                        null,
                        LocalDateTime.now()
                );
            }

            student.setDepartment(department);

            department.setCurrentStrength(
                    department.getCurrentStrength() + 1
            );

            student.setLastDepartmentChangeDate(LocalDate.now());

            Student savedStudent = studentRepo.save(student);

            log.info("Department {} assigned successfully to student {} for tenant {}",
                    deptId, studId, tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Department assigned successfully",
                    studentMapper.toDto(savedStudent),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Department assignment failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error during department assignment", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<StudentResponseDto> updateStudentById(Long studId, StudentRequestDto studentRequestDto) {

        try {

            log.info("Updating student with id {}", studId);

            String tenantId = TenantContext.getTenant();

            Student existingStudent = studentRepo
                    .findByStudIdAndTenantId(studId, tenantId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id " + studId));

            if (!existingStudent.getEmail().equals(studentRequestDto.getEmail())) {

                if (studentRepo.existsByEmailAndTenantId(studentRequestDto.getEmail(), tenantId)) {

                    return new ApiResponse<>(
                            false,
                            HttpStatus.CONFLICT,
                            "Another student with this email already exists in this tenant",
                            null,
                            LocalDateTime.now()
                    );
                }
            }

            existingStudent.setFirstName(studentRequestDto.getFirstName());
            existingStudent.setLastName(studentRequestDto.getLastName());
            existingStudent.setEmail(studentRequestDto.getEmail());
            existingStudent.setAge(studentRequestDto.getAge());
            existingStudent.setCGpa(studentRequestDto.getCGpa());

            Student savedStudent = studentRepo.save(existingStudent);

            StudentResponseDto response = studentMapper.toDto(savedStudent);

            log.info("Student updated successfully with id {}", studId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Student updated successfully",
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

            log.error("Unexpected error while updating student", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<StudentResponseDto> enrollCourse(Long studentId, Long courseId) {

        log.info("Attempting enrollment for student {} into course {}", studentId, courseId);

        try {

            if (studentId == null || courseId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "StudentId and CourseId are required",
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

            Student student = studentRepo
                    .findByStudIdAndTenantId(studentId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found with id " + studentId)
                    );

            Course course = courseRepo
                    .findByCourseIdAndTenantId(courseId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Course not found with id " + courseId)
                    );

            if (!student.isActive()) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Inactive student cannot enroll",
                        null,
                        LocalDateTime.now()
                );
            }

            if (course.getDeadLine() != null &&
                    course.getDeadLine().isBefore(LocalDate.now())) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Enrollment deadline has passed",
                        null,
                        LocalDateTime.now()
                );
            }

            if (student.getCourses().contains(course)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Student already enrolled in this course",
                        null,
                        LocalDateTime.now()
                );
            }

            if (course.getStudents().size() >= course.getMaxCapacity()) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Course capacity is full",
                        null,
                        LocalDateTime.now()
                );
            }

            if (student.getCourses().size() >= MAX_ALLOWED_COURSES) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Maximum course enrollment limit reached",
                        null,
                        LocalDateTime.now()
                );
            }

            Float cgpa = student.getCGpa();

            if (cgpa == null || cgpa < 0 || cgpa > 10) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Invalid CGPA value",
                        null,
                        LocalDateTime.now()
                );
            }

            if (cgpa < 6.0) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student not eligible due to low CGPA",
                        null,
                        LocalDateTime.now()
                );
            }

            if (student.getGraduationDate() != null &&
                    student.getGraduationDate().isBefore(LocalDate.now().plusMonths(2))) {

                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student cannot enroll due to graduation within 2 months",
                        null,
                        LocalDateTime.now()
                );
            }

            double baseFee = course.getCourseFee();

            double finalFee = CgpaHelper.calculateDiscount(baseFee, cgpa);

            BankAccount bankAccount = student.getBankAccount();

            if (bankAccount == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student does not have a bank account",
                        null,
                        LocalDateTime.now()
                );
            }

            if (bankAccount.getBalance() < finalFee) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Insufficient balance",
                        null,
                        LocalDateTime.now()
                );
            }

            double newBalance = bankAccount.getBalance() - finalFee;

            bankAccount.setBalance(
                    Math.round(newBalance * 100.0) / 100.0
            );

            student.getCourses().add(course);
            course.getStudents().add(student);

            studentRepo.save(student);

            log.info("Enrollment successful for student {} into course {} for tenant {}",
                    studentId, courseId, tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Enrollment successful. Final fee deducted: " + finalFee,
                    studentMapper.toDto(student),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Enrollment failed: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error during enrollment: {}", e.getMessage());

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong during enrollment",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<StudentResponseDto> unenrollStudentFromCourse(
            Long studentId,
            Long courseId) {

        log.info("Student {} attempting to unenroll from course {}", studentId, courseId);

        try {

            if (studentId == null || courseId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "StudentId and CourseId are required",
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

            Student student = studentRepo
                    .findByStudIdAndTenantId(studentId, tenantId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Course course = courseRepo
                    .findByCourseIdAndTenantId(courseId, tenantId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (!student.getCourses().contains(course)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Student is not enrolled in this course",
                        null,
                        LocalDateTime.now()
                );
            }

            Float cgpa = student.getCGpa();
            double baseFee = course.getCourseFee();

            double refundAmount = CgpaHelper.calculateRefund(
                    baseFee,
                    cgpa,
                    course.getDeadLine()
            );

            BankAccount bankAccount = student.getBankAccount();

            if (bankAccount == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Bank account not found for refund",
                        null,
                        LocalDateTime.now()
                );
            }

            if (refundAmount > 0) {
                double newBalance = bankAccount.getBalance() + refundAmount;

                bankAccount.setBalance(
                        Math.round(newBalance * 100.0) / 100.0
                );
            }

            student.getCourses().remove(course);
            course.getStudents().remove(student);

            studentRepo.save(student);

            String message = refundAmount > 0
                    ? "Student unenrolled successfully. Refund amount: " + refundAmount
                    : "Student unenrolled successfully. No refund (deadline passed)";

            log.info("Student {} unenrolled from course {} for tenant {}",
                    studentId, courseId, tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    message,
                    studentMapper.toDto(student),
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
                    "Something went wrong during unenrollment",
                    null,
                    LocalDateTime.now()
            );
        }
    }
    public ApiResponse<List<StudentResponseDto>> getAllStudents() {

        try {
            log.info("Trying to fetch all students");

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

            List<Student> students = studentRepo.findAllByTenantId(tenantId);

            for (Student student : students) {

                if (student.getIdCard() != null &&
                        student.getIdCard().getExpiryDate() != null &&
                        student.getIdCard().getExpiryDate().isBefore(LocalDate.now())) {

                    student.getIdCard().setActive(false);
                    idCardRepo.save(student.getIdCard());
                }
                if (student.getGraduationDate().isBefore(LocalDate.now())) {
                    student.setDepartment(null);
                }
            }

            log.info("Number of students available for tenant {}: {}", tenantId, students.size());

            List<StudentResponseDto> response =
                    studentMapper.toDtoList(students);

            log.info("Students fetched successfully");

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Students fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch students", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch students",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<StudentResponseDto> updateGraduationDate(
            Long studentId,
            LocalDate newGraduationDate) {

        try {

            log.info("Updating graduation date for student {}", studentId);

            if (studentId == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Student id must not be null",
                        null,
                        LocalDateTime.now()
                );
            }

            if (newGraduationDate == null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Graduation date must not be null",
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

            Student student = studentRepo
                    .findByStudIdAndTenantId(studentId, tenantId)
                    .orElseThrow(() ->
                            new RuntimeException("Student not found with id: " + studentId));

            student.setGraduationDate(newGraduationDate);

            if (student.getIdCard() != null) {

                IdCard idCard = student.getIdCard();
                idCard.setExpiryDate(newGraduationDate);

                if (newGraduationDate.isBefore(LocalDate.now())) {
                    idCard.setActive(false);
                } else {
                    idCard.setActive(true);
                }

                idCardRepo.save(idCard);
            }

            studentRepo.save(student);

            log.info("Graduation date updated successfully for tenant {}", tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Graduation date updated successfully",
                    studentMapper.toDto(student),
                    LocalDateTime.now()
            );

        } catch (RuntimeException e) {

            log.warn("Failed to update graduation date");

            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Unexpected error while updating graduation date", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<StudentResponseDto>> getAllStudentsWithTenants() {
        try {

            String tenantId=TenantContext.getTenant();
            log.info("Trying to fetch all students from tenant {}",tenantId);

            List<Student> students = studentRepo.findAllByTenantId(tenantId);

            log.info("Number of students available in this tenant: {}", students.size());

            List<StudentResponseDto> response =
                    studentMapper.toDtoList(students);

            log.info("Students fetched successfully from the tenant {}",tenantId);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Students fetched successfully",
                    response,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            log.error("Failed to fetch students", e);

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch students",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<String> deactivateStudents() {
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

            List<Student> students = studentRepo.findAllByTenantId(tenantId);
            LocalDate today = LocalDate.now();

            for (Student student : students) {

                if (student.getGraduationDate() != null &&
                        today.isAfter(student.getGraduationDate())) {

                    student.setActive(false);
                }

                if (student.getIdCard() != null &&
                        student.getIdCard().getIssueDate() != null &&
                        today.isAfter(student.getIdCard().getIssueDate())) {

                    student.getIdCard().setActive(false);
                }

                studentRepo.save(student);
            }

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Students checked and deactivated if needed",
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while processing students",
                    null,
                    LocalDateTime.now()
            );
        }
    }
}
