package com.example.MiniProject.Dto.Requests;

import com.example.MiniProject.Dto.DepartmentSummaryDto;
import com.example.MiniProject.Entity.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private Integer age;

}
