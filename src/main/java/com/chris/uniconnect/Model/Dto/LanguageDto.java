package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.LanguageLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import org.mapstruct.Mapping;

//hay un error con el language Nivel
@Data
public class LanguageDto {

    private Integer idIdioma;

    private Integer idPersona;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private LanguageLevel nivel;

}
