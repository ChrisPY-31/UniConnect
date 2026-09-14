package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.PersonMappers;
import com.chris.uniconnect.Mappers.RecruiterMappers;
import com.chris.uniconnect.Mappers.StudentMappers;
import com.chris.uniconnect.Mappers.TeacherMappers;
import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Model.Entity.Aptitude;
import com.chris.uniconnect.Model.Entity.Person;
import com.chris.uniconnect.Model.Entity.Recruiter;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Model.Entity.UserEntity;
import com.chris.uniconnect.Repository.*;
import com.chris.uniconnect.Service.IPersonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class PersonaServiceImpl implements IPersonService {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepostory teacherRepostory;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AptitudeRepository aptitudeRepository;

    private static final int MAX_APTITUDES = 5;


    @Override
    @Transactional
    public void deletePerson(PersonDto person) {
        UserEntity userEntity = userRepository.findByPersonId(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "idPersona", person.getId()));

        userEntity.setEnabled(false);
        userEntity.setAccountNonLocked(false);
        userRepository.save(userEntity);
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

        RecruiterDto recruiter = RecruiterMappers.INSTANCE.recruiterToRecruiterDto(recruiterRepository.findById(id).orElse(null));
        if (recruiter != null) {
            return recruiter;
        }
        throw new ResourceNotFoundException("cliente", "id", id);

    }

    @Override
    public PersonDto getPersonByUserName(String username) {

        StudentDto student = PersonMappers.INSTANCE.studentToStudentDto(studentRepository.findByUserEntityUsername(username));
        if (student != null) {
            return student;
        }
        TeacherDto teacher = TeacherMappers.INSTANCE.teacherToTeacherDto(teacherRepostory.findByUserEntityUsername(username));
        if (teacher != null) {
            return teacher;
        }

        RecruiterDto reruiter = RecruiterMappers.INSTANCE.recruiterToRecruiterDto(recruiterRepository.findByUserEntityUsername(username));

        if (reruiter != null) {
            return reruiter;
        }



        throw new ResourceNotFoundException("cliente", "id", username);
    }

    @Override
    @Transactional
    public PersonDto updateAptitudes(String username, Set<Integer> aptitudeIds) {
        if (aptitudeIds == null || aptitudeIds.isEmpty()) {
            throw new BadRequestException("Debes seleccionar al menos una aptitud");
        }
        if (aptitudeIds.size() > MAX_APTITUDES) {
            throw new BadRequestException("Solo puedes seleccionar hasta " + MAX_APTITUDES + " aptitudes");
        }

        List<Aptitude> aptitudesEncontradas = aptitudeRepository.findAllById(aptitudeIds);
        if (aptitudesEncontradas.size() != aptitudeIds.size()) {
            throw new BadRequestException("Alguna de las aptitudes seleccionadas no existe");
        }
        Set<Aptitude> aptitudes = new HashSet<>(aptitudesEncontradas);

        Student student = studentRepository.findByUserEntityUsername(username);
        if (student != null) {
            student.setAptitudes(aptitudes);
            return PersonMappers.INSTANCE.studentToStudentDto(studentRepository.save(student));
        }

        Teacher teacher = teacherRepostory.findByUserEntityUsername(username);
        if (teacher != null) {
            teacher.setAptitudes(aptitudes);
            return TeacherMappers.INSTANCE.teacherToTeacherDto(teacherRepostory.save(teacher));
        }

        Recruiter recruiter = recruiterRepository.findByUserEntityUsername(username);
        if (recruiter != null) {
            recruiter.setAptitudes(aptitudes);
            return RecruiterMappers.INSTANCE.recruiterToRecruiterDto(recruiterRepository.save(recruiter));
        }

        throw new ResourceNotFoundException("cliente", "username", username);
    }

}
