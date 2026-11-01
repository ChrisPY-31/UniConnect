package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.ConversationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {

    private Long idConversacion;

    private ConversationStatus estado;

    private LocalDateTime creada;

    private LocalDateTime ultimoMensajeEn;

    private MessageDto ultimoMensaje;

    //mensajes de la otra parte que quien consulta no ha leido
    private long noLeidos;

    //vacante de la postulacion con la que se abrio el chat
    private JobApplicationJobDto vacante;

    private ChatParticipantDto contraparte;
}
