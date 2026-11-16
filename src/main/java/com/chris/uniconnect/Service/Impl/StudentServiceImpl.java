package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.PersonMappers;
import com.chris.uniconnect.Mappers.StudentMappers;
import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.StudentAllDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.StudentSearchRequest;
import com.chris.uniconnect.Model.Dto.Response.StudentStatsResponse;
import com.chris.uniconnect.Model.Entity.Person;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Repository.StudentSpecifications;
import com.chris.uniconnect.Service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public StudentDto getStudentById(Integer id) {
        return PersonMappers.INSTANCE.studentToStudentDto(studentRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentAllDto> getStudents(StudentSearchRequest search, Pageable pageable) {
        Specification<Student> specification = Specification.allOf(
                StudentSpecifications.text(search.q()),
                StudentSpecifications.career(search.idCareer()),
                StudentSpecifications.semester(search.semester()),
                StudentSpecifications.ubication(search.idUbication()),
                StudentSpecifications.allTechnologies(search.idTechnologies()),
                StudentSpecifications.withProjects(search.withProjects()),
                StudentSpecifications.recommended(search.recommended()),
                search.byRecommendations() ? StudentSpecifications.orderByRecommendations() : null
        );
        return studentRepository.findAll(specification, pageable).map(StudentMappers.INSTANCE::studentToStudentAllDto);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentStatsResponse getStudentStats() {
        List<StudentStatsResponse.CareerCount> byCareer = studentRepository.countStudentsByCareer().stream()
                .map(row -> new StudentStatsResponse.CareerCount((Integer) row[0], (String) row[1], ((Number) row[2]).longValue()))
                .toList();
        return new StudentStatsResponse(studentRepository.count(), byCareer, studentRepository.countByIdCareerIsNull());
    }

    @Override
    public StudentDto createStudent(StudentDto studentDto) {
        return PersonMappers.INSTANCE.studentToStudentDto(studentRepository.save(PersonMappers.INSTANCE.studentDtoToStudent(studentDto)));
    }

    public StudentDto updateStudent(StudentDto studentDto) {
        // Buscar el estudiante actual
        Student existingStudent = studentRepository.findById(studentDto.getId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + studentDto.getId()));

        // Aplicar solo los campos no nulos del DTO sobre la entidad existente
        PersonMappers.INSTANCE.updateStudentFromDto(studentDto, existingStudent);

        // Guardar y devolver
        return PersonMappers.INSTANCE.studentToStudentDto(studentRepository.save(existingStudent));
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
