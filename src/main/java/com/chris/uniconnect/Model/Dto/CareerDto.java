package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CareerDto {

    private Integer idCarrera;

    @NotBlank(message = "El nombre de la carrera es obligatorio")
    private String carrera;

}
