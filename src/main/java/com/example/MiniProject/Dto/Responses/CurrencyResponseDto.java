package com.example.MiniProject.Dto.Responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponseDto {

    private Long id;

    private String currencyCode;

    private Double valueInr;
}
