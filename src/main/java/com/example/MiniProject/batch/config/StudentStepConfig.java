
package com.example.MiniProject.batch.config;

import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.batch.dto.StudentCsvDto;
import com.example.MiniProject.batch.writer.StudentWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;

@Configuration
@RequiredArgsConstructor
public class StudentStepConfig {

    @Bean
    public Step studentStep(JobRepository jobRepository,
                            FlatFileItemReader<StudentCsvDto> studentReader,
                            ItemProcessor<StudentCsvDto, Student> processor,
                            StudentWriter writer) {

        return new StepBuilder("studentStep", jobRepository)
                .<StudentCsvDto, Student>chunk(100)
                .reader(studentReader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}