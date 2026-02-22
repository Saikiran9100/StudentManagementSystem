package com.example.MiniProject.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class ApiResponse <T>{

    private boolean success;
    private HttpStatus status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
