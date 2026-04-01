package com.example.MiniProject.Dto.Requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequestDto {

    @Pattern(
            regexp = "^[A-Za-z]{3,5}$",
            message = "Currency code must contain letters between 3-5 chrachters"
    )
    private String currencyCode;

    @NotNull(message = "value should not be null")
    private Double valueInr;
}
