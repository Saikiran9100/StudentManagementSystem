package com.example.MiniProject.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdCardSummaryDto {

    private Long id;
    private String cardNumber;
    private boolean active;
    private String section;
    private String address;
}