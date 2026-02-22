package com.example.MiniProject.Mapper;


import com.example.MiniProject.Dto.DepartmentSummaryDto;
import com.example.MiniProject.Dto.Requests.DepartmentRequestDto;
import com.example.MiniProject.Dto.Responses.DepartmentResponseDto;
import com.example.MiniProject.Entity.Department;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toEntity(DepartmentRequestDto departmentRequestDto);

    DepartmentResponseDto toDto(Department department);

    DepartmentSummaryDto toSummaryDto(Department department);

    List<DepartmentResponseDto> toDtoList(List<Department> departments);
}
