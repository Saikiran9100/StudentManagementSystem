package com.example.MiniProject.Mapper;


import com.example.MiniProject.Dto.Requests.CurrencyRequestDto;
import com.example.MiniProject.Dto.Responses.CurrencyResponseDto;
import com.example.MiniProject.Entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    List<CurrencyResponseDto> toDtoList(List<Currency> currencyList);

    CurrencyResponseDto toDto(Currency currency);

    Currency toEntity(CurrencyRequestDto currencyRequestDto);
}
