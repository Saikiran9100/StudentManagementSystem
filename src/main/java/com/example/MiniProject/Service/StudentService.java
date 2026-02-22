package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.Requests.StudentRequestDto;
import com.example.MiniProject.Dto.Responses.StudentResponseDto;
import com.example.MiniProject.Entity.Course;
import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.StudentMapper;
import com.example.MiniProject.Repository.CourseRepo;
import com.example.MiniProject.Repository.DepartmentRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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


    public ApiResponse<StudentResponseDto> addStudent(StudentRequestDto studentRequestDto) {

        log.info("Trying to add student");

        try {

            Student student = studentMapper.toEntity(studentRequestDto);


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
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<List<StudentResponseDto>> getAllStudents() {
        try {
            log.info("fetching students");
            List<Student> students = studentRepo.findAll();
            List<StudentResponseDto> response = studentMapper.toDtoList(students);
            log.info("total students fetched {}", response.size());
            return new ApiResponse<List<StudentResponseDto>>(
                    true,
                    HttpStatus.OK,
                    "students fetched successfully",
                    response,
                    LocalDateTime.now()


            );
        } catch (RuntimeException e) {
            log.warn("Error while fetching students", e);
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()

            );
        }
    }

    public ApiResponse<StudentResponseDto> getStudentById(Long studId) {
        try {
            log.info("Fetching student with id: {}", studId);
            Student student = studentRepo.findById(studId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id :" + studId));
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
            log.warn("student not found with id {}", studId);
            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<Void> deleteStudent(Long studId) {
        try {
            log.info("Attempting to delete student id {}", studId);
            Student student = studentRepo.findById(studId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id"));
            studentRepo.deleteById(studId);
            log.info("student deleted successfully");
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "student successfully deleted",
                    null,
                    LocalDateTime.now()
            );
        } catch (RuntimeException e) {
            log.warn("student with id is not found {}", studId);
            return new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
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

    public ApiResponse<StudentResponseDto> updateStudentAndDepartment(Long deptId,Long studId, StudentRequestDto requestDto) {

        log.info("Updating student with id {}", studId);

        try {

            Student existingStudent = studentRepo.findById(studId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id " + studId));


            if (deptId== null) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Department id is required",
                        null,
                        LocalDateTime.now()
                );
            }

            Department department = departmentRepo.findById(deptId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            existingStudent.setFirstName(requestDto.getFirstName());
            existingStudent.setLastName(requestDto.getLastName());
            existingStudent.setEmail(requestDto.getEmail());
            existingStudent.setAge(requestDto.getAge());

            existingStudent.setDepartment(department);


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

    public ApiResponse<StudentResponseDto> updateStudentById(Long studId, StudentRequestDto studentRequestDto) {


        try {
            log.info("Updating student with id {}", studId);
            Student existingStudent = studentRepo.findById(studId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id " + studId));
            existingStudent.setFirstName(studentRequestDto.getFirstName());
            existingStudent.setLastName(studentRequestDto.getLastName());
            existingStudent.setEmail(studentRequestDto.getEmail());
            existingStudent.setAge(studentRequestDto.getAge());
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

    public ApiResponse<StudentResponseDto> enrollStudentInCourse(
            Long studentId,
            Long courseId) {

        log.info("Student {} enrolling in course {}", studentId, courseId);

        try {

            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // prevent duplicate enrollment
            if (student.getCourses().contains(course)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Student already enrolled in this course",
                        null,
                        LocalDateTime.now()
                );
            }

            // update both sides
            student.getCourses().add(course);
            course.getStudents().add(student);

            studentRepo.save(student); // owner side

            StudentResponseDto response = studentMapper.toDto(student);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Student enrolled successfully",
                    response,
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
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }

    public ApiResponse<StudentResponseDto> unenrollStudentFromCourse(
            Long studentId,
            Long courseId) {

        log.info("Student {} unenrolling from course {}", studentId, courseId);

        try {

            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Check if student is actually enrolled
            if (!student.getCourses().contains(course)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Student is not enrolled in this course",
                        null,
                        LocalDateTime.now()
                );
            }

            // 🔥 Remove relationship from BOTH sides
            student.getCourses().remove(course);
            course.getStudents().remove(student);

            // Save owner side (Student owns join table)
            studentRepo.save(student);

            StudentResponseDto response = studentMapper.toDto(student);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Student unenrolled successfully",
                    response,
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
                    "Something went wrong",
                    null,
                    LocalDateTime.now()
            );
        }
    }
}