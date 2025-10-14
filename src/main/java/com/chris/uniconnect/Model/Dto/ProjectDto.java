package com.chris.uniconnect.Model.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder

public class ProjectDto {

    private Integer id;

    private String nombre;

    private String descripcion;

    private String imagen;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;


}
