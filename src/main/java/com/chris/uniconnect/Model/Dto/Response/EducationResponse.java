package com.chris.uniconnect.Model.Dto.Response;

import com.chris.uniconnect.Enum.EducationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationResponse {
    private Integer idEducacion;

    private String institucion;

    private String grado;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EducationType educacionTipo;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
