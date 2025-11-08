package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String nombre;

    private String descripcion;

    private String imagen;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String github;

    private String deploy;

    private Set<StudentAllDto> menciones;

    private List<TechnologyDto> tecnologias;


}
