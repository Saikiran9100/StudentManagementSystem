
package com.example.MiniProject.batch.writer;

import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.Repository.StudentRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentDepartmentUpdateWriter implements ItemWriter<Student> {

    private final StudentRepo studentRepo;

    @Override
    public void write(Chunk<? extends Student> chunk) {
        studentRepo.saveAll(chunk.getItems());
    }
}