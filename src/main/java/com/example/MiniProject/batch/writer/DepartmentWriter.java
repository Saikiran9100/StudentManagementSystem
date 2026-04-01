package com.example.MiniProject.batch.writer;

import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.Repository.DepartmentRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentWriter implements ItemWriter<Department> {

    private final DepartmentRepo departmentRepo;

    @Override
    public void write(Chunk<? extends Department> chunk) {

        departmentRepo.saveAll(chunk.getItems());
    }
}