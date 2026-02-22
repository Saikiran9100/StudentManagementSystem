package com.example.MiniProject.Dto.Responses;


import com.example.MiniProject.Dto.StudentSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentResponseDto {

    private Long deptId;
    private String deptName;

    private List<StudentSummaryDto> students;
}
