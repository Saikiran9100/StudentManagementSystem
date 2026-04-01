package com.example.MiniProject.Dto.Responses;

import com.example.MiniProject.Dto.StudentSummaryDto;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {

    private Long courseId;
    private String courseName;
    private Double courseFee;
    private Integer maxCapacity;
    private String tenantId;
    private List<StudentSummaryDto> students;

}