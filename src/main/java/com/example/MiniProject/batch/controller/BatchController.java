

package com.example.MiniProject.batch.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.job.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MiniProject.batch.service.BatchService;
import com.example.MiniProject.Response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/batch")
public class BatchController {

    private final BatchService batchService;

    private final Job studentJob;
    private final Job departmentJob;
    private final Job studentDepartmentUpdateJob;
@PostMapping("/students")
public ResponseEntity<ApiResponse<String>> uploadStudents(
        @RequestParam("file") MultipartFile file) {

    ApiResponse<String> response = batchService.startBatch(studentJob,file);
    return new ResponseEntity<>(response, response.getStatus());
}

    @PostMapping("/departments")
    public ResponseEntity<ApiResponse<String>> uploadDepartments(
            @RequestParam("file") MultipartFile file
    ) {
        ApiResponse<String> response = batchService.startBatch(departmentJob,file);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/student-department-update")
    public ResponseEntity<ApiResponse<String>> updateStudentDepartment(
            @RequestParam("file") MultipartFile file
    ) {
        ApiResponse<String> response = batchService.startBatch(studentDepartmentUpdateJob,file);
        return new ResponseEntity<>(response, response.getStatus());
    }
}