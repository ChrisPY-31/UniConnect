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
    public TeacherDto createStudent(TeacherDto teacher) {
        return TeacherMappers.INSTANCE.teacherToTeacherDto(
                teacherRepostory.save(TeacherMappers.INSTANCE.teacherDtoToTeacher(teacher)));
    }

    @Override
    public TeacherDto updateStudent(TeacherDto teacher) {
        return TeacherMappers.INSTANCE.teacherToTeacherDto(
                teacherRepostory.save(TeacherMappers.INSTANCE.teacherDtoToTeacher(teacher)));
    }

    @Override
    public boolean existStudent(int id) {
        return teacherRepostory.existsById(id);
    }
}
