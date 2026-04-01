
package com.example.MiniProject.batch.reader;

import com.example.MiniProject.batch.dto.StudentDepartmentUpdateCsvDto;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class StudentDepartmentUpdateReaderConfig {
    @Bean
    @StepScope
    public FlatFileItemReader<StudentDepartmentUpdateCsvDto> studentDepartmentUpdateReader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        return new FlatFileItemReaderBuilder<StudentDepartmentUpdateCsvDto>()
                .name("studentDepartmentUpdateReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("studentId", "deptId")
                .targetType(StudentDepartmentUpdateCsvDto.class)
                .linesToSkip(1)
                .saveState(false)
                .build();
    }
}