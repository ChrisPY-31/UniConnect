package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UbicationDto {

    private Integer idUbicacion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
