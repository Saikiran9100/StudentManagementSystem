package com.example.MiniProject.Dto.Responses;


import com.example.MiniProject.Dto.StudentSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdCardResponseDto {

    private Long id;
    private String cardNumber;
    private String standard;
    private String section;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private boolean active;
    private String address;
    private String tenantId;

    private StudentSummaryDto student;
}
