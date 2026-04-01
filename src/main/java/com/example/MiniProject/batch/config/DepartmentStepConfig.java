package com.example.MiniProject.batch.config;

import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.batch.dto.DepartmentCsvDto;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;

@Configuration
@RequiredArgsConstructor
public class DepartmentStepConfig {

    @Bean
    public Step departmentStep(JobRepository jobRepository,
                               FlatFileItemReader<DepartmentCsvDto> departmentReader,
                               ItemProcessor<DepartmentCsvDto, Department> processor,
                               ItemWriter<Department> writer) {

        return new StepBuilder("departmentStep", jobRepository)
                .<DepartmentCsvDto, Department>chunk(100)
                .reader(departmentReader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}