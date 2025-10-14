package com.chris.uniconnect.Service;


import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.StudentAllDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IStudentService {
    Page<StudentAllDto> getStudents(Pageable pageable);

    StudentDto createStudent(StudentDto studentDto);

    StudentDto updateStudent(StudentDto studentDto);

    boolean existStudent(int id);

    List<StudentAllDto> getStudentByName(String name );

}
