package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.Requests.CourseRequestDto;
import com.example.MiniProject.Dto.Responses.CourseResponseDto;
import com.example.MiniProject.Entity.Course;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController

@RequestMapping("/api/course")
public class CourseController {

    private  final CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CourseResponseDto>> addCourse(@RequestBody CourseRequestDto courseRequestDto){
        ApiResponse<CourseResponseDto> response=courseService.addCourse(courseRequestDto);
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
    public ResponseEntity<ApiResponse<CourseResponseDto>> updateCourse(@PathVariable Long courseId,@RequestBody CourseRequestDto courseRequestDto){
        ApiResponse<CourseResponseDto> response=courseService.updateCourse(courseId,courseRequestDto);
        return new ResponseEntity<>(response,response.getStatus());

    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long courseId){
        ApiResponse<Void> response=courseService.deleteCourse(courseId);
        return new ResponseEntity<>(response,response.getStatus());
    }


}
