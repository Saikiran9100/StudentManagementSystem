package com.example.MiniProject.Controller;


import com.example.MiniProject.Dto.DepartmentSummaryDto;
import com.example.MiniProject.Dto.Requests.DepartmentRequestDto;
import com.example.MiniProject.Dto.Responses.DepartmentResponseDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Response.PageResponse;
import com.example.MiniProject.Service.CourseService;
import com.example.MiniProject.Service.DepartmentService;
import jakarta.validation.Valid;
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
    private final CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> addDepartment(@Valid @RequestBody DepartmentRequestDto departmentRequestDto){
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
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> updateDepartment(@PathVariable Long deptId,@Valid @RequestBody DepartmentRequestDto departmentRequestDto){
        ApiResponse<DepartmentResponseDto> response=departmentService.updateDepartment(deptId,departmentRequestDto);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/getpages")
    public ResponseEntity<ApiResponse<PageResponse<DepartmentSummaryDto>>> getAllDepartmentsWithPagination(
            @RequestParam(required = false,defaultValue = "0") int pageNo,
            @RequestParam(required = false,defaultValue = "5") int pageSize,
            @RequestParam(required = false,defaultValue = "deptId") String sortBy,
            @RequestParam(required = false,defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String deptCode,
            @RequestParam(required = false) String deptName
    ){
        ApiResponse<PageResponse<DepartmentSummaryDto>> response=departmentService.getAllDepartmentsWithPagination(pageNo,pageSize,sortBy,sortDir,deptId,deptCode,deptName);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping("/tenants")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDepartmentsWithTenants(){
        ApiResponse<List<DepartmentResponseDto>> resposne=departmentService.getAllDepartmentsWithTenants();
        return new  ResponseEntity<>(resposne,resposne.getStatus());
    }
}
