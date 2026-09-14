package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AptitudeDto {

    private Integer idAptitud;

    @NotBlank(message = "El nombre de la aptitud es obligatorio")
    private String nombre;

}
