package com.chris.uniconnect.Model.Dto;

import lombok.*;

//resumen publico de la empresa con convenio, para perfiles de reclutador (y despues vacantes)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanySummaryDto {

    private Long idCorporacion;

    private String nombreEmpresa;

    private String categoria;

    private String logo;
}
