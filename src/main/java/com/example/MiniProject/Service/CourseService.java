package com.example.MiniProject.Service;


import com.example.MiniProject.Dto.Requests.CourseRequestDto;
import com.example.MiniProject.Dto.Responses.CourseResponseDto;
import com.example.MiniProject.Entity.Course;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.CourseMapper;
import com.example.MiniProject.Repository.CourseRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepo courseRepo;
    private final CourseMapper courseMapper;
    private final StudentRepo studentRepo;

    public ApiResponse<CourseResponseDto> addCourse(CourseRequestDto courseRequestDto) {
        try {
            log.info("Attempting to Add New Course");
            Course course = courseMapper.toEntity(courseRequestDto);
            CourseResponseDto response = courseMapper.toDto(courseRepo.save(course));
            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    ":Course added Successfully",
                    response,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            log.warn("Failed to Add course");
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }
        public ApiResponse<List<CourseResponseDto>> getAllCourses(){
            try{
                log.info("Fetching all Available Courses");
                List<Course> courses=courseRepo.findAll();
                log.info("No of Courses availbale {}",courses.size());
                List<CourseResponseDto> response=courseMapper.toDtoList(courses);

                return new ApiResponse<List<CourseResponseDto>>(
                        true,
                        HttpStatus.OK,
                        "Courses fetched Successflly",
                        response,
                        LocalDateTime.now()

                );
            } catch (Exception e) {
                log.warn("failed to Load Courses");
                return new ApiResponse<>(
                        false,
                        HttpStatus.NOT_FOUND,
                        e.getMessage(),
                        null,
                        LocalDateTime.now()
                );
            }
        }

    public ApiResponse<CourseResponseDto> getCourseById(Long courseId) {

        try{
            log.info("Trying to Fetch course Bt Id");
            Course exists=courseRepo.findById(courseId)
                    .orElseThrow(()->new RuntimeException("course not Found With given id"));
            log.info("Course fetched Successfully");
            CourseResponseDto response=courseMapper.toDto(exists);
            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Course fetched Successfully",
                    response,
                    LocalDateTime.now()
            );
        } catch (RuntimeException e) {
            log.warn("Failed to fetch Course");
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

    public ApiResponse<CourseResponseDto> updateCourse(Long courseId,CourseRequestDto courseRequestDto) {

        try {
            log.info("trying to update coursename");
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("course id not found"));

            course.setCourseName(courseRequestDto.getCourseName());
            Course saved = courseRepo.save(course);
            CourseResponseDto dto = courseMapper.toDto(saved);
            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Course updated Successfully",
                    dto,
                    LocalDateTime.now()
            );
        } catch (RuntimeException e) {
            log.warn("failed to update course");
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

    public ApiResponse<Void> deleteCourse(Long courseId) {
        try {

            log.info("trying to delete course ");
            Course course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id"));
            courseRepo.deleteById(courseId);
            return new ApiResponse<Void>(
                    true,
                    HttpStatus.OK,
                    "Course deleted SuccessFully",
                    null,
                    LocalDateTime.now()
            );
        } catch (RuntimeException e) {
            log.warn("Failed to delete Course");
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
}
