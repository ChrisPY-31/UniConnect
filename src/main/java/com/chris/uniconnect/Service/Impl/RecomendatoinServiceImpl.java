package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.RecomendationMappers;
import com.chris.uniconnect.Model.Dto.RecomendationDto;
import com.chris.uniconnect.Model.Entity.Recomendation;
import com.chris.uniconnect.Model.Entity.RecomendationPk;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Repository.RecomendationRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Repository.TeacherRepostory;
import com.chris.uniconnect.Service.IRecomendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecomendatoinServiceImpl implements IRecomendationService {

    @Autowired
    private RecomendationRepository recomendationRepository;

    @Autowired
    private TeacherRepostory teacherRepostory;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<RecomendationDto> getRecomendations() {
        return RecomendationMappers.INSTANCE.listRecomendationToListDto(recomendationRepository.findAll());
    }

    @Override
    public RecomendationDto createRecomendation(String username, RecomendationDto recomendationDto) {
        Teacher teacher = requireTeacher(username);

        if (recomendationDto.getId() == null || recomendationDto.getId().getIdStudent() == null) {
            throw new BadRequestException("Se requiere el id del estudiante para la recomendacion");
        }
        Integer idStudent = recomendationDto.getId().getIdStudent();
        if (!studentRepository.existsById(idStudent)) {
            throw new ResourceNotFoundException("Estudiante", "id", idStudent);
        }

        recomendationDto.setId(new RecomendationPk(idStudent, teacher.getId()));

        Recomendation saved = recomendationRepository.save(RecomendationMappers.INSTANCE.recomendationDtoToRecomendation(recomendationDto));
        return RecomendationMappers.INSTANCE.recomendationToRecomendationDto(saved);
    }

    @Override
    public RecomendationDto updateRecomendation(String username, Integer idStudent, RecomendationDto recomendationDto) {
        Teacher teacher = requireTeacher(username);

        RecomendationPk id = new RecomendationPk(idStudent, teacher.getId());
        if (!recomendationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recomendacion", "idStudent-idTeacher", idStudent + "-" + teacher.getId());
        }

        recomendationDto.setId(id);
        Recomendation saved = recomendationRepository.save(RecomendationMappers.INSTANCE.recomendationDtoToRecomendation(recomendationDto));
        return RecomendationMappers.INSTANCE.recomendationToRecomendationDto(saved);
    }

    @Override
    public void deleteRecomendation(String username, Integer idStudent) {
        Teacher teacher = requireTeacher(username);

        RecomendationPk id = new RecomendationPk(idStudent, teacher.getId());
        if (!recomendationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recomendacion", "idStudent-idTeacher", idStudent + "-" + teacher.getId());
        }

        recomendationRepository.deleteById(id);
    }

    private Teacher requireTeacher(String username) {
        Teacher teacher = teacherRepostory.findByUserEntityUsername(username);
        if (teacher == null) {
            throw new ResourceNotFoundException("Profesor", "username", username);
        }
        return teacher;
    }
}
