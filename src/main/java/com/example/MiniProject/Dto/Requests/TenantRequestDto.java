package com.example.MiniProject.Dto.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TenantRequestDto {

    @NotBlank(message = "Tenant id is required")
    @Pattern(
            regexp = "^[A-Za-z0-9_-]{2,30}$",
            message = "Tenant id must be 2-30 characters and use only letters, numbers, underscores, or hyphens"
    )
    private String tenantId;
}
