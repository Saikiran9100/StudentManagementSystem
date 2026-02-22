package com.example.MiniProject.Mapper;


import com.example.MiniProject.Dto.Requests.IdCardRequestDto;
import com.example.MiniProject.Dto.Responses.IdCardResponseDto;
import com.example.MiniProject.Entity.IdCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = StudentMapper.class)
public interface IdCardMapper {

    @Mapping(target = "student", ignore = true)
    IdCard toEntity(IdCardRequestDto idCardRequestDto);


    IdCardResponseDto toDto(IdCard idCard);

    List<IdCardResponseDto> toDtoList(List<IdCard> idCardList);
}
