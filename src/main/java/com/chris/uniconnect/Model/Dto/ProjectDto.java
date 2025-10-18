package com.chris.uniconnect.Model.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder

public class ProjectDto {

    private Integer idProject;

    private Integer idEstudiante;

    private String nombre;

    private String descripcion;

    private String imagen;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Set<StudentAllDto> menciones;

    private List<ProjectLinksDto> redes;

    private List<TechnologyDto> tecnologias;


}
