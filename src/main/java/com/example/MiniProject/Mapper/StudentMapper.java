package com.example.MiniProject.Mapper;


import com.example.MiniProject.Dto.Requests.StudentRequestDto;
import com.example.MiniProject.Dto.Responses.StudentResponseDto;
import com.example.MiniProject.Dto.StudentSummaryDto;
import com.example.MiniProject.Entity.Student;
import com.example.MiniProject.batch.dto.StudentCsvDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
@Mapper(
        componentModel = "spring",
        uses = {DepartmentMapper.class, IdCardMapper.class}
)
public interface StudentMapper {

    @Mapping(source = "tenantId", target = "tenantId")
    StudentResponseDto toDto(Student student);

    @Mapping(target = "department", ignore = true)
    Student toEntity(StudentRequestDto studentRequestDto);

    StudentSummaryDto toSummaryDto(Student student);

    List<StudentResponseDto> toDtoList(List<Student> students);

    List<StudentSummaryDto> toSummaryDtoList(List<Student> students);

    Student toEntityForCSv(StudentCsvDto studentCsvDto);


}