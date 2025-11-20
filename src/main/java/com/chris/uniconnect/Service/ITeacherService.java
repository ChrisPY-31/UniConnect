package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.Response.TeacherDetailResponse;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Model.Entity.Teacher;

import java.util.List;

public interface ITeacherService {

    List<TeacherDetailResponse> getAllTeachers();

    TeacherDto getTeacherById(int id);

    TeacherDto createTeacher(TeacherDto teacher);

    TeacherDto updateTeacher(TeacherDto teacher);

    boolean existStudent(int id);
}
