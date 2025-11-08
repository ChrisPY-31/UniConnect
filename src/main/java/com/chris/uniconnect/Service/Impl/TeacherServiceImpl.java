package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.TeacherMappers;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Repository.TeacherRepostory;
import com.chris.uniconnect.Service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements ITeacherService {

    @Autowired
    private TeacherRepostory teacherRepostory;

    @Override
    public TeacherDto getTeacherById(int id) {
        return TeacherMappers.INSTANCE.teacherToTeacherDto(teacherRepostory.findById(id).orElse(null));
    }

    @Override
    public TeacherDto createTeacher(TeacherDto teacher) {
        return TeacherMappers.INSTANCE.teacherToTeacherDto(
                teacherRepostory.save(TeacherMappers.INSTANCE.teacherDtoToTeacher(teacher)));
    }

    @Override
    public TeacherDto updateTeacher(TeacherDto teacherDto) {
        Teacher existingTeacher = teacherRepostory.findById(teacherDto.getId())
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + teacherDto.getId()));

        Teacher updatedTeacher = TeacherMappers.INSTANCE.teacherDtoToTeacher(teacherDto);

        updatedTeacher.setUserEntity(existingTeacher.getUserEntity());

        return TeacherMappers.INSTANCE.teacherToTeacherDto(teacherRepostory.save(updatedTeacher));
    }


    @Override
    public boolean existStudent(int id) {
        return teacherRepostory.existsById(id);
    }
}
