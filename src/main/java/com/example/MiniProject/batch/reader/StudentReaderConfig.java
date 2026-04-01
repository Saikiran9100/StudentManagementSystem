
package com.example.MiniProject.batch.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.example.MiniProject.batch.dto.StudentCsvDto;

@Configuration
public class StudentReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<StudentCsvDto> studentReader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        return new FlatFileItemReaderBuilder<StudentCsvDto>()
                .name("studentReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("firstName", "lastName", "email", "age", "cGpa")
                .targetType(StudentCsvDto.class)
                .linesToSkip(1)
                .build();
    }
}