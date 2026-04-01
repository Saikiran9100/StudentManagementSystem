


package com.example.MiniProject.batch.processor;

import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Mapper.StudentMapper;
import com.example.MiniProject.Repository.StudentRepo;
import com.example.MiniProject.batch.dto.StudentCsvDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class StudentProcessor implements ItemProcessor<StudentCsvDto, Student> {

    private final StudentRepo studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public Student process(StudentCsvDto dto) {

        if (studentRepository.existsByEmail(dto.getEmail())) {
            return null;
        }

        Student student = studentMapper.toEntityForCSv(dto);

        LocalDate admission = LocalDate.now();
        student.setAdmissionDate(admission);
        student.setGraduationDate(admission.plusYears(4));
        student.setActive(true);

        return student;
    }
}