package com.example.MiniProject.batch.reader;

import com.example.MiniProject.batch.dto.DepartmentCsvDto;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class DepartmentReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<DepartmentCsvDto> departmentReader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        return new FlatFileItemReaderBuilder<DepartmentCsvDto>()
                .name("departmentReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("deptName", "deptCode", "capacity")
                .targetType(DepartmentCsvDto.class)
                .linesToSkip(1)
                .build();
    }
}