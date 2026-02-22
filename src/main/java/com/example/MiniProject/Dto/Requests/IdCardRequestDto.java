package com.example.MiniProject.Dto.Requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IdCardRequestDto {

    private String standard;
    private String section;
    private String address;

}
