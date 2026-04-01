package com.example.MiniProject.Dto.Responses;

import com.example.MiniProject.Dto.CourseSummaryDto;
import com.example.MiniProject.Dto.DepartmentSummaryDto;
import com.example.MiniProject.Dto.IdCardSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {

    private Long studId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
    private Float cGpa;
    private String tenantId;
    private IdCardSummaryDto idCard;
    private DepartmentSummaryDto department;
    private Set<CourseSummaryDto> courses;
}
