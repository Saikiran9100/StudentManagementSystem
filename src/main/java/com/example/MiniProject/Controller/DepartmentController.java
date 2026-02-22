package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.Requests.DepartmentRequestDto;
import com.example.MiniProject.Dto.Responses.DepartmentResponseDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> addDepartment(@RequestBody DepartmentRequestDto departmentRequestDto){
        ApiResponse<DepartmentResponseDto> response=departmentService.addDepartment(departmentRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDepartments(){
        ApiResponse<List<DepartmentResponseDto>> resposne=departmentService.getAllDepartments();
        return new  ResponseEntity<>(resposne,resposne.getStatus());
    }

    @GetMapping("/get/{deptId}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDepartmentById(@PathVariable Long deptId){
        ApiResponse<DepartmentResponseDto> response=departmentService.getDepartmentById(deptId);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @DeleteMapping("/delete/{deptId}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long deptId){
        ApiResponse<Void> response=departmentService.deleteDepartment(deptId);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PutMapping("/update/{deptId}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> updateDepartment(@PathVariable Long deptId,@RequestBody DepartmentRequestDto departmentRequestDto){
        ApiResponse<DepartmentResponseDto> response=departmentService.updateDepartment(deptId,departmentRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }
}
