package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.PersonMappers;
import com.chris.uniconnect.Mappers.StudentMappers;
import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.StudentAllDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Entity.Person;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Page<StudentAllDto> getStudents(Pageable pageable) {
        return studentRepository.findAll(pageable).map(StudentMappers.INSTANCE::studentToStudentAllDto);
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        return PersonMappers.INSTANCE.studentToStudentDto(studentRepository.save(PersonMappers.INSTANCE.studentDtoToStudent(studentDto)));
    }

    public StudentDto updateStudent(StudentDto studentDto) {
        return PersonMappers.INSTANCE.studentToStudentDto(studentRepository.save(PersonMappers.INSTANCE.studentDtoToStudent(studentDto)));
    }

    @Override
    public boolean existStudent(int id) {
        return studentRepository.existsById(id);
    }

    @Override
    public List<StudentAllDto> getStudentByName(String name) {
        return studentRepository.findAllByName(name).stream().map(StudentMappers.INSTANCE::studentToStudentAllDto).toList();


    }


}
