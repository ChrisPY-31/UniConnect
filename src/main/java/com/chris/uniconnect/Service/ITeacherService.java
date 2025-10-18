package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Model.Entity.Teacher;

public interface ITeacherService {

    TeacherDto getTeacherById(int id);

    TeacherDto createStudent(TeacherDto teacher);

    TeacherDto updateStudent(TeacherDto teacher);

    boolean existStudent(int id);
}
