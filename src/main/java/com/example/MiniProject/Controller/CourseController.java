package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.CourseSummaryDto;
import com.example.MiniProject.Dto.Requests.CourseRequestDto;
import com.example.MiniProject.Dto.Responses.CourseResponseDto;
import com.example.MiniProject.Entity.Course;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController

@RequestMapping("/api/course")
public class CourseController {

    private  final CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CourseResponseDto>> addCourse(@RequestParam String currencyCode,@Valid @RequestBody CourseRequestDto courseRequestDto){
        ApiResponse<CourseResponseDto> response=courseService.addCourse(currencyCode,courseRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get")
     public ResponseEntity<ApiResponse<List<CourseResponseDto>>> getAllCourses(){
        ApiResponse<List<CourseResponseDto>> response=courseService.getAllCourses();
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get/{courseid}")
    public ResponseEntity<ApiResponse<CourseResponseDto>> getCourseById(@PathVariable Long courseid){
        ApiResponse<CourseResponseDto> response=courseService.getCourseById(courseid);
        return new ResponseEntity<>(response,response.getStatus());
    }
    @PutMapping("/update/{courseId}")
    public ResponseEntity<ApiResponse<CourseResponseDto>> updateCourse(@PathVariable Long courseId,@RequestParam String currencyCode,@Valid @RequestBody CourseRequestDto courseRequestDto){
        ApiResponse<CourseResponseDto> response=courseService.updateCourse(courseId,currencyCode,courseRequestDto);
        return new ResponseEntity<>(response,response.getStatus());

    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long courseId){
        ApiResponse<Void> response=courseService.deleteCourse(courseId);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/getpages")
    public ResponseEntity<ApiResponse<PageResponse<CourseSummaryDto>>> getAllCoursesWithPagination(
            @RequestParam(required = false,defaultValue = "0") int pageNo,
            @RequestParam(required = false,defaultValue = "5")int pageSize,
            @RequestParam(required = false,defaultValue = "courseId") String sortBy,
            @RequestParam(required = false,defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long courseId,
            @RequestParam (required = false)String courseName,
            @RequestParam(required = false)LocalDate deadLine
            ){
        ApiResponse<PageResponse<CourseSummaryDto>> response=courseService.getAllCoursesWithPagination(pageNo-1,pageSize,sortBy,
                sortDir,courseId,courseName,deadLine);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/tenants")
    public ResponseEntity<ApiResponse<List<CourseResponseDto>>> getAllCoursesWithTenants(){
        ApiResponse<List<CourseResponseDto>> response=courseService.getAllCoursesWithTenants();
        return new ResponseEntity<>(response,response.getStatus());
    }

}
