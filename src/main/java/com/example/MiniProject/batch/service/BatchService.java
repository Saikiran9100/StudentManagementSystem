
package com.example.MiniProject.batch.service;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.MiniProject.Response.ApiResponse;

import java.io.File;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final JobOperator jobOperator;

    public ApiResponse<String> startBatch(Job job, MultipartFile file) {

        try {

            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File tempFile = new File(uploadDir + file.getOriginalFilename());

            file.transferTo(tempFile);


            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", tempFile.getAbsolutePath())
                    .addLong("run.id", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobOperator.start(job, jobParameters);

            return new ApiResponse<>(
                    true,
                    HttpStatus.OK,
                    "Batch Started Successfully. Execution ID: " + execution.getId(),
                    null,
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            return new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Batch Failed: " + e.getMessage(),
                    null,
                    LocalDateTime.now()
            );
        }
    }
}