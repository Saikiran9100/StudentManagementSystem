package com.example.MiniProject.Dto.Responses;

import com.example.MiniProject.Dto.StudentSummaryDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankAccountResponseDto {

    private Long bankId;
    private String bankName;
    private String accountNo;
    private Double balance;
    private String tenantId;

    private StudentSummaryDto student;
}