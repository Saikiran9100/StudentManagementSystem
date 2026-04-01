package com.example.MiniProject.Mapper;


import com.example.MiniProject.Dto.DepartmentSummaryDto;
import com.example.MiniProject.Dto.Requests.DepartmentRequestDto;
import com.example.MiniProject.Dto.Responses.DepartmentResponseDto;
import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.batch.dto.DepartmentCsvDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    Department toEntity(DepartmentRequestDto departmentRequestDto);

    @Mapping(source = "tenantId", target = "tenantId")
    DepartmentResponseDto toDto(Department department);

    DepartmentSummaryDto toSummaryDto(Department department);

    Department toEntityFromCsv(DepartmentCsvDto departmentCsvDto);
    List<DepartmentResponseDto> toDtoList(List<Department> departments);

    List<DepartmentSummaryDto> toSummaryDtoList(List<Department> departments);
}
