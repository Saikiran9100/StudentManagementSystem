package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.Requests.StudentRequestDto;
import com.example.MiniProject.Dto.Responses.StudentResponseDto;
import com.example.MiniProject.Dto.StudentSummaryDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<StudentResponseDto>> addStudent(@Valid  @RequestBody StudentRequestDto studentRequestDto){
        ApiResponse<StudentResponseDto> response=studentService.addStudent(studentRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/tenants")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudentsWithTenants(){
        ApiResponse<List<StudentResponseDto>> response=studentService.getAllStudentsWithTenants();
        return new ResponseEntity<>(response,response.getStatus());
    }
    @GetMapping("/getpages")
    public ResponseEntity<ApiResponse<PageResponse<StudentSummaryDto>>> getAllStudentsWithPagination(
            @RequestParam(required = false,defaultValue = "0") int pageNo,
            @RequestParam(required = false,defaultValue = "5") int pageSize,
            @RequestParam(required = false,defaultValue = "studId") String sortBy,
            @RequestParam(required = false,defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Float cGpa){
        ApiResponse<PageResponse<StudentSummaryDto>> response=studentService.getAllStudentsWithPagination(pageNo-1,pageSize,sortBy,sortDir,email,cGpa);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/getstudents")
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
    public ResponseEntity<ApiResponse<StudentResponseDto>> assignDepartment(@PathVariable Long studId,@RequestParam Long deptId){
        ApiResponse<StudentResponseDto> response=studentService.assignDepartment(studId,deptId);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PutMapping("/update/{studId}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudentById(@PathVariable Long studId,@Valid @RequestBody StudentRequestDto studentRequestDto){
        ApiResponse<StudentResponseDto> response=studentService.updateStudentById(studId,studentRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }
    @PutMapping("/update/{studId}/enroll")
    public ResponseEntity<ApiResponse<StudentResponseDto>> enrollCourse(
            @PathVariable Long studId,
            @RequestParam Long courseId) {

        ApiResponse<StudentResponseDto> response =
                studentService.enrollCourse(studId, courseId);

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

    @PatchMapping("/{id}/graduation-date")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateGraduationDate(
            @PathVariable Long id,
            @RequestParam LocalDate graduationDate) {

        ApiResponse<StudentResponseDto> response =
                studentService.updateGraduationDate(id, graduationDate);

        return new ResponseEntity<>(response, response.getStatus());
    }
    @PatchMapping("/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivateStudents() {

        ApiResponse<String> response = studentService.deactivateStudents();

        return new ResponseEntity<>(response, response.getStatus());
    }
}
