package com.example.MiniProject.batch.dto;

import lombok.Data;

@Data
public class StudentCsvDto {

    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Float cGpa;
}