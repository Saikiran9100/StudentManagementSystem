package com.example.MiniProject.Service;

import com.example.MiniProject.Dto.Requests.TenantRequestDto;
import com.example.MiniProject.Entity.TenantRegistry;
import com.example.MiniProject.Repository.BankAccountRepo;
import com.example.MiniProject.Repository.CourseRepo;
import com.example.MiniProject.Repository.DepartmentRepo;
import com.example.MiniProject.Repository.IdCardRepo;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Repository.TenantRegistryRepo;
import com.example.MiniProject.Response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final StudentRepo studentRepo;
    private final DepartmentRepo departmentRepo;
    private final CourseRepo courseRepo;
    private final BankAccountRepo bankAccountRepo;
    private final IdCardRepo idCardRepo;
    private final TenantRegistryRepo tenantRegistryRepo;

    public List<String> getAllTenantIds() {
        Set<String> tenantIds = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        tenantIds.addAll(
                tenantRegistryRepo.findAllByOrderByTenantIdAsc()
                        .stream()
                        .map(TenantRegistry::getTenantId)
                        .toList()
        );
        tenantIds.addAll(studentRepo.findDistinctTenantIds());
        tenantIds.addAll(departmentRepo.findDistinctTenantIds());
        tenantIds.addAll(courseRepo.findDistinctTenantIds());
        tenantIds.addAll(bankAccountRepo.findDistinctTenantIds());
        tenantIds.addAll(idCardRepo.findDistinctTenantIds());
        return tenantIds.stream().toList();
    }

    public ApiResponse<String> addTenant(TenantRequestDto tenantRequestDto) {
        try {
            String normalizedTenantId = tenantRequestDto.getTenantId().trim().toUpperCase();

            if ("DEFAULT".equalsIgnoreCase(normalizedTenantId)) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.BAD_REQUEST,
                        "Default tenant is reserved",
                        null,
                        LocalDateTime.now()
                );
            }

            if (getAllTenantIds().stream().anyMatch(existingTenantId -> existingTenantId.equalsIgnoreCase(normalizedTenantId))) {
                return new ApiResponse<>(
                        false,
                        HttpStatus.CONFLICT,
                        "Tenant already exists",
                        null,
                        LocalDateTime.now()
                );
            }

            TenantRegistry tenantRegistry = new TenantRegistry();
            tenantRegistry.setTenantId(normalizedTenantId);
            tenantRegistryRepo.save(tenantRegistry);

            return new ApiResponse<>(
                    true,
                    HttpStatus.CREATED,
                    "Tenant created successfully",
                    normalizedTenantId,
                    LocalDateTime.now()
            );
        } catch (Exception exception) {
            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while creating tenant",
                    null,
                    LocalDateTime.now()
            );
        }
    }
}
