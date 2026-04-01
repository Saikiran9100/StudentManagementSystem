package com.example.MiniProject.Mapper;

import com.example.MiniProject.Dto.CourseSummaryDto;
import com.example.MiniProject.Dto.Requests.CourseRequestDto;
import com.example.MiniProject.Dto.Responses.CourseResponseDto;
import com.example.MiniProject.Entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface CourseMapper {

    Course toEntity(CourseRequestDto dto);


    @Mapping(source = "tenantId", target = "tenantId")
    CourseResponseDto toDto(Course course);

    CourseSummaryDto toSummaryDto(Course course);

    List<CourseResponseDto> toDtoList(List<Course> courses);

    List<CourseSummaryDto> toSummarayDtoList(List<Course> courses);

}