package com.example.MiniProject.Dto.Requests;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequestDto {


    @NotBlank(message = "Bankname should not be Blank or Empty")
    private String bankName;
    @NotBlank(message = "Account number is required")
    @Pattern(
            regexp = "^[0-9]{10,18}$",
            message = "Account number must contain only digits and be 10 to 18 characters long"
    )
    private String accountNo;

    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance cannot be negative")
    private Double balance;

    @NotBlank(message = "branch name should not be empty or blank")
    private String branch;
}
