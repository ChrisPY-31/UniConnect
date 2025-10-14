package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.PersonMappers;
import com.chris.uniconnect.Mappers.RecruiterMappers;
import com.chris.uniconnect.Mappers.TeacherMappers;
import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Model.Entity.Person;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Repository.PersonaRepository;
import com.chris.uniconnect.Repository.RecruiterRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Repository.TeacherRepostory;
import com.chris.uniconnect.Service.IPersonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PersonaServiceImpl implements IPersonService {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepostory teacherRepostory;

    @Autowired
    private RecruiterRepository recruiterRepository;


    @Override
    public void deletePerson(PersonDto person) {

    }

    @Override
    @Transactional
    public PersonDto getPersonsById(Integer id) {

        StudentDto student = PersonMappers.INSTANCE.studentToStudentDto(studentRepository.findById(id).orElse(null));

        if (student != null) {
            return student;
        }
        TeacherDto teacher = TeacherMappers.INSTANCE.teacherToTeacherDto(teacherRepostory.findById(id).orElse(null));
        if (teacher != null) {
            return teacher;
        }

        RecruiterDto reruiter = RecruiterMappers.INSTANCE.recruiterToRecruiterDto(recruiterRepository.findById(id).orElse(null));
        if (reruiter != null) {
            return reruiter;
        }

        throw new RuntimeException("Persona no encontrada con el id: " + id);
    }


    /*
    @Override
    public PersonDto getPersonsById(Integer id) {
        return PersonMappers personaRepository.findById(id).orElse(null);
    }

     */


}
