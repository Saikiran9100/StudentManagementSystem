package com.example.MiniProject.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseSummaryDto {

    private Long courseId;
    private String courseName;
    private LocalDate deadLine;
    private Double courseFee;

}