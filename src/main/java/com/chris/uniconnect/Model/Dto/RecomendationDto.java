package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Dto.Response.TeacherDetailResponse;
import com.chris.uniconnect.Model.Entity.RecomendationPk;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecomendationDto {

    private RecomendationPk id;

    private String comentario;

    private LocalDate fechaRecomendacion = LocalDate.now();

    private TeacherDetailResponse maestro;
}
