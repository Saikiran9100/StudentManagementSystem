package com.example.MiniProject.Dto.Requests;

import com.example.MiniProject.Dto.DepartmentSummaryDto;
import com.example.MiniProject.Entity.Department;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDto {

    @NotBlank(message = "FirstName Should not be blank")
    private String firstName;
    @NotBlank(message = "LastName Should not be blank")
    private String lastName;

    @Pattern(
            regexp = "^[A_Za-z0-9._+]+@gmail\\.com$",
            message = "Email should be in valid format"
    )
    private String email;

    @Min(value = 16 ,message = "Student minimum allowed age is 16")
    @Max(value = 28,message = "maximum age is allowed is below 28")
    private Integer age;

    private LocalDate admissionDate;

    @Positive(message = "cgpa always positive")
    @NotNull
    private Float cGpa;

}
