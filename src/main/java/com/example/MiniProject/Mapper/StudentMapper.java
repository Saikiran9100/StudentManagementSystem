package com.example.MiniProject.Mapper;


import com.example.MiniProject.Dto.Requests.StudentRequestDto;
import com.example.MiniProject.Dto.Responses.StudentResponseDto;
import com.example.MiniProject.Dto.StudentSummaryDto;
import com.example.MiniProject.Entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(
        componentModel = "spring",
        uses = {DepartmentMapper.class}
)
public interface StudentMapper {

    StudentResponseDto toDto(Student student);

    @Mapping(target = "department", ignore = true)
    Student toEntity(StudentRequestDto studentRequestDto);

    StudentSummaryDto toSummaryDto(Student student);

    List<StudentResponseDto> toDtoList(List<Student> students);
}