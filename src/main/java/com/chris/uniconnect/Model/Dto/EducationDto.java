package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.EducationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationDto {

    private Integer idEducacion;

    private Integer idPersona;

    private String institucion;

    private String grado;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EducationType educacionTipo;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
