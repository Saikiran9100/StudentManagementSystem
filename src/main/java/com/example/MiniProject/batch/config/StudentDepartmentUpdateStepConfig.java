
package com.example.MiniProject.batch.config;

import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.batch.dto.StudentDepartmentUpdateCsvDto;
import com.example.MiniProject.batch.writer.StudentDepartmentUpdateWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;

@Configuration
@RequiredArgsConstructor
public class StudentDepartmentUpdateStepConfig {

    @Bean
    public Step studentDepartmentUpdateStep(
            JobRepository jobRepository,

            @Qualifier("studentDepartmentUpdateReader")
            FlatFileItemReader<StudentDepartmentUpdateCsvDto> reader,

            ItemProcessor<StudentDepartmentUpdateCsvDto, Student> processor,

            StudentDepartmentUpdateWriter writer) {

        return new StepBuilder("studentDepartmentUpdateStep", jobRepository)
                .<StudentDepartmentUpdateCsvDto, Student>chunk(100)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}