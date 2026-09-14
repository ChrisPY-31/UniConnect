package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.EducationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationDto {

    private Integer idEducacion;

    private Integer idPersona;

    @NotBlank(message = "La institucion es obligatoria")
    private String institucion;

    @NotBlank(message = "El grado es obligatorio")
    private String grado;

    private String descripcion;

    @NotNull(message = "El tipo de educacion es obligatorio")
    private EducationType educacionTipo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
