package com.example.MiniProject.Dto.Requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdCardRequestDto {

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @NotBlank(message = "Standard is required")
    private String standard;

    @NotBlank(message = "Section is required")
    private String section;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    private LocalDate expiryDate;

    @NotBlank(message = "Address is required")
    private String address;
}