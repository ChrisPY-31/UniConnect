package com.chris.uniconnect.Model.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AptitudeDto {

    private Integer idAptitud;

    private Integer idPersona;

    private String nombre;


}
