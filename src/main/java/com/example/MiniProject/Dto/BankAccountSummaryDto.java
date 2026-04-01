package com.example.MiniProject.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountSummaryDto {

    private Long bankId;
    private String accountNo;
    private String bankName;
    private String branch;
    private Double balance;

}
