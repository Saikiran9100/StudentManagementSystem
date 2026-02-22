package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.Requests.StudentRequestDto;
import com.example.MiniProject.Dto.Responses.StudentResponseDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<StudentResponseDto>> addStudent(@RequestBody StudentRequestDto studentRequestDto){
        ApiResponse<StudentResponseDto> response=studentService.addStudent(studentRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudents(){
        ApiResponse<List<StudentResponseDto>> response=studentService.getAllStudents();
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get/{studId}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(@PathVariable Long studId){
        ApiResponse<StudentResponseDto> response=studentService.getStudentById(studId);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @DeleteMapping("/delete/{studId}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long studId){

        ApiResponse<Void> response=studentService.deleteStudent(studId);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PutMapping("/updatedept/{studId}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudentAndDepartment(@RequestParam Long deptId,@PathVariable Long studId,@RequestBody StudentRequestDto studentRequestDto){
        ApiResponse<StudentResponseDto> response=studentService.updateStudentAndDepartment(deptId,studId,studentRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PutMapping("/update/{studId}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudentById(@PathVariable Long studId,@RequestBody StudentRequestDto studentRequestDto){
        ApiResponse<StudentResponseDto> response=studentService.updateStudentById(studId,studentRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }
    @PutMapping("/update/{studId}/enroll")
    public ResponseEntity<ApiResponse<StudentResponseDto>> enrollCourse(
            @PathVariable Long studId,
            @RequestParam Long courseId) {

        ApiResponse<StudentResponseDto> response =
                studentService.enrollStudentInCourse(studId, courseId);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/update/{studId}/unenroll")
    public ResponseEntity<ApiResponse<StudentResponseDto>> unenrollCourse(
            @PathVariable Long studId,
            @RequestParam Long courseId) {

        ApiResponse<StudentResponseDto> response =
                studentService.unenrollStudentFromCourse(studId, courseId);

        return new ResponseEntity<>(response, response.getStatus());
    }

}
