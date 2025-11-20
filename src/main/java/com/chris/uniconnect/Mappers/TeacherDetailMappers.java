package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.Response.TeacherDetailResponse;
import com.chris.uniconnect.Model.Dto.Response.TeacherResponse;
import com.chris.uniconnect.Model.Entity.Teacher;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TeacherDetailMappers {

    TeacherDetailMappers INSTANCE = Mappers.getMapper(TeacherDetailMappers.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "lastName", target = "apellido")
    @Mapping(source = "image", target = "imagen")
    TeacherDetailResponse teacherToTeacherDetailResponse(Teacher teacher);

    @InheritInverseConfiguration

    Teacher teacherDetailResponseToTeacher(TeacherDetailResponse teacherDetailResponse);

    List<TeacherDetailResponse> teacherListToTeacherDetailResponse(List<Teacher> teachers);

    List<Teacher> teacherDetailListToTeacher(List<TeacherDetailResponse> teacherDetailResponses);

}
