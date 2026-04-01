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
public class StudentJobConfig {

    @Bean
    public Job studentJob(JobRepository jobRepository, Step studentStep) {

        return new JobBuilder("studentJob", jobRepository)
                .start(studentStep)
                .build();
    }
}