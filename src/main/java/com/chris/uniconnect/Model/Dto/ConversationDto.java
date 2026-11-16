package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.ConversationStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {

    private Long idConversacion;

    private ConversationStatus estado;

    private OffsetDateTime creada;

    private OffsetDateTime ultimoMensajeEn;

    private MessageDto ultimoMensaje;

    //mensajes de la otra parte que quien consulta no ha leido
    private long noLeidos;

    //vacante de la postulacion con la que se abrio el chat; null en los chats directos
    private JobApplicationJobDto vacante;

    private ChatParticipantDto contraparte;
}
