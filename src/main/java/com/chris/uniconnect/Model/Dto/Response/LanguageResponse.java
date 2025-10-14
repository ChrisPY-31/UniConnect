package com.chris.uniconnect.Model.Dto.Response;

import com.chris.uniconnect.Enum.LanguageLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class LanguageResponse {
    private Integer idIdioma;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private LanguageLevel nivel;

}
