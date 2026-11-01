package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.AplicationsStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationDto {

    private Long idPostulacion;

    private AplicationsStatus estado;

    private String mensaje;

    private String curriculum;

    private LocalDateTime fechaPostulacion;

    private LocalDateTime actualizada;

    private JobApplicationJobDto vacante;

    private JobApplicantDto estudiante;
}
