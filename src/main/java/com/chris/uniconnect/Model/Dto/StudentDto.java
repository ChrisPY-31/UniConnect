package com.chris.uniconnect.Model.Dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto extends PersonDto {

    private Integer idCarrera;

    private String semestre;

    private CareerDto carrera;

    private List<ProjectDto> proyectos;

    private List<RecomendationDto> recomendaciones;
}
