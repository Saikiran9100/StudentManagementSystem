package com.example.MiniProject.Mapper;


import com.example.MiniProject.Dto.IdCardSummaryDto;
import com.example.MiniProject.Dto.Requests.IdCardRequestDto;
import com.example.MiniProject.Dto.Responses.IdCardResponseDto;
import com.example.MiniProject.Entity.IdCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IdCardMapper {

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "active", ignore = true)
    IdCard toEntity(IdCardRequestDto idCardRequestDto);


    @Mapping(source = "tenantId", target = "tenantId")
    IdCardResponseDto toDto(IdCard idCard);

    List<IdCardResponseDto> toDtoList(List<IdCard> idCardList);


    List<IdCardSummaryDto> toSummaryDtoList(List<IdCard> content);
}
