package com.example.MiniProject.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;

@Configuration
@RequiredArgsConstructor
public class DepartmentJobConfig {

    @Bean
    public Job departmentJob(JobRepository jobRepository, Step departmentStep) {

        return new JobBuilder("departmentJob", jobRepository)
                .start(departmentStep)
                .build();
    }
}