package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Entity.RecomendationPk;
import com.chris.uniconnect.Model.Entity.Teacher;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecomendationDto {

    private RecomendationPk id;

    private String comentario;

    private LocalDate fechaRecomendacion = LocalDate.now();

    private TeacherDto maestro;
}
