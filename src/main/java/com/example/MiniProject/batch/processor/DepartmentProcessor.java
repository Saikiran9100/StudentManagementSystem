package com.example.MiniProject.batch.processor;

import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.Mapper.DepartmentMapper;
import com.example.MiniProject.Repository.DepartmentRepo;
import com.example.MiniProject.batch.dto.DepartmentCsvDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.batch.infrastructure.item.ItemProcessor;

@Component
@RequiredArgsConstructor
public class DepartmentProcessor implements ItemProcessor<DepartmentCsvDto, Department> {

    private final DepartmentRepo departmentRepo;
    private final DepartmentMapper departmentMapper;

    @Override
    public Department process(DepartmentCsvDto dto) {

        if (departmentRepo.existsByDeptCode(dto.getDeptCode())) {
            return null;
        }

        Department department=departmentMapper.toEntityFromCsv(dto);

        return department;
    }
}