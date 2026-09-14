package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.LanguageLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LanguageDto {

    private Integer idIdioma;

    private Integer idPersona;

    @NotBlank(message = "El nombre del idioma es obligatorio")
    private String nombre;

    @NotNull(message = "El nivel es obligatorio")
    private LanguageLevel nivel;

}
