

package com.example.MiniProject.batch.processor;

import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.Repository.DepartmentRepo;
import com.example.MiniProject.batch.dto.StudentDepartmentUpdateCsvDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.batch.infrastructure.item.ItemProcessor;

@Component
@RequiredArgsConstructor
public class StudentDepartmentUpdateProcessor
        implements ItemProcessor<StudentDepartmentUpdateCsvDto, Student> {

    private final StudentRepo studentRepo;
    private final DepartmentRepo departmentRepo;
    @Override
    public Student process(StudentDepartmentUpdateCsvDto dto) {

        Student student = studentRepo.findById(dto.getStudentId()).orElse(null);
        if (student == null) {
            return null;
        }

        Department department = departmentRepo.findById(dto.getDeptId()).orElse(null);
        if (department == null) {
            return null;
        }

        student.setDepartment(department);

        return student;
    }
}