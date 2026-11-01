package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.VacanteStatus;
import com.chris.uniconnect.Enum.WorkModality;
import com.chris.uniconnect.Enum.WorkingDay;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostJobDto {

    private Long idVacante;

    private String titulo;

    private String descripcion;

    private String ubicacion;

    private WorkingDay jornada;

    private WorkModality modalidad;

    private TypeApplication tipo;

    private Integer salarioMin;

    private Integer salarioMax;

    //estado efectivo: incluye VENCIDA cuando ya paso la fecha de cierre
    private VacanteStatus estado;

    private LocalDate fechaPublicacion;

    private LocalDate fechaCierre;

    private LocalDateTime creada;

    private LocalDateTime actualizada;

    private List<CareerDto> carreras;

    private List<TechnologyDto> tecnologias;

    private CompanySummaryDto empresa;

    private JobRecruiterDto reclutador;
}
