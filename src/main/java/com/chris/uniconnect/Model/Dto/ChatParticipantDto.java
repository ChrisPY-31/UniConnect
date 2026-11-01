package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.IssuerType;
import lombok.*;

//la otra persona de la conversacion, vista desde quien consulta
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatParticipantDto {

    private Integer id;

    private IssuerType tipo;

    private String nombre;

    private String apellido;

    private String imagen;

    //solo para reclutadores
    private CompanySummaryDto empresa;
}
