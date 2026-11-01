package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.VacanteStatus;
import lombok.*;

//resumen de la vacante dentro de una postulacion
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationJobDto {

    private Long idVacante;

    private String titulo;

    private VacanteStatus estado;

    private CompanySummaryDto empresa;
}
