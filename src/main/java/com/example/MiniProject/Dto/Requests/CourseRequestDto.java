package com.example.MiniProject.Dto.Requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {
    @NotBlank(message = "CourseName should not be empty")
    private String courseName;

    @NotNull(message = "Course fee is required")
    @Positive(message = "Course fee must be greater than zero")
    private Double courseFee;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be in the future")
    private LocalDate deadLine;

    @NotNull(message = "Max capacity is required")
    @Positive
    @Max(value = 200, message = "Max capacity must be greater than zero and less than 200")
    private Integer maxCapacity;

}