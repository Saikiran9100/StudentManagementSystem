package com.example.MiniProject.Controller;

import com.example.MiniProject.Dto.Requests.TenantRequestDto;
import com.example.MiniProject.Response.ApiResponse;
import com.example.MiniProject.Service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addTenant(@Valid @RequestBody TenantRequestDto tenantRequestDto) {
        ApiResponse<String> response = tenantService.addTenant(tenantRequestDto);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<String>>> getTenantIds() {
        ApiResponse<List<String>> response = new ApiResponse<>(
                true,
                HttpStatus.OK,
                "Tenants fetched successfully",
                tenantService.getAllTenantIds(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, response.getStatus());
    }
}
