package com.example.MiniProject.Mapper;

import com.example.MiniProject.Dto.BankAccountSummaryDto;
import com.example.MiniProject.Dto.Requests.BankAccountRequestDto;
import com.example.MiniProject.Dto.Responses.BankAccountResponseDto;
import com.example.MiniProject.Entity.BankAccount;
import com.example.MiniProject.Response.PageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring",uses = StudentMapper.class)
public interface BankAccountMapper {


    @Mapping(target = "student", ignore = true)
    BankAccount toEntity(BankAccountRequestDto dto);


    @Mapping(source = "tenantId", target = "tenantId")
    BankAccountResponseDto toDto(BankAccount entity);

    @Mapping(target = "bankId", ignore = true)
    @Mapping(target = "student", ignore = true)
     void updateEntityFromDto(
            BankAccountRequestDto dto,
            @MappingTarget BankAccount entity
    );

    BankAccountSummaryDto toSummaryDto(BankAccount bankAccount);

    List<BankAccountResponseDto> toDtoList(List<BankAccount> bankAccountList);

    List<BankAccountSummaryDto> toSummaryDtoList(List<BankAccount> bankAccounts);
}
