package com.example.MiniProject.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentSummaryDto {

    private Long studId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private Float cGpa;
}
