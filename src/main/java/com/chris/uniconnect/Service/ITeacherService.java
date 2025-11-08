package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Model.Entity.Teacher;

public interface ITeacherService {

    TeacherDto getTeacherById(int id);

    TeacherDto createTeacher(TeacherDto teacher);

    TeacherDto updateTeacher(TeacherDto teacher);

    boolean existStudent(int id);
}
