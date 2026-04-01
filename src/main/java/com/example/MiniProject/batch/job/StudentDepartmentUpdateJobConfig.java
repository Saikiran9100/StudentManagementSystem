

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
public class StudentDepartmentUpdateJobConfig {

    @Bean
    public Job studentDepartmentUpdateJob(JobRepository jobRepository,
                                          Step studentDepartmentUpdateStep) {

        return new JobBuilder("studentDepartmentUpdateJob", jobRepository)
                .start(studentDepartmentUpdateStep)
                .build();
    }
}