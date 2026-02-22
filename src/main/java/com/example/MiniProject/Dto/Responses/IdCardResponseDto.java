package com.example.MiniProject.Dto.Responses;


import com.example.MiniProject.Dto.StudentSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class IdCardResponseDto {

    private Long id;
    private String standard;
    private String section;
    private String address;
    private StudentSummaryDto student;

}
