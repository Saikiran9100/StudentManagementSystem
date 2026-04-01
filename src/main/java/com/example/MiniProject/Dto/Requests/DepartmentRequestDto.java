package com.example.MiniProject.Dto.Requests;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentRequestDto {

    @NotEmpty(message = "Department should not be empty or null")
    private String deptName;

    @Pattern(
            regexp = "^[A-Za-z]{2,5}$",
            message = "Department code must contaion letters between 2-5 chrachters"
    )
    private String deptCode;

    @Positive(message = "capacity must be greater than 0")
    @Max(value = 150)
    private Long capacity;
}
