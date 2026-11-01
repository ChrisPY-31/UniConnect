package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.IssuerType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long idMensaje;

    private Long idConversacion;

    private Integer idRemitente;

    private IssuerType remitente;

    private String contenido;

    private LocalDateTime enviado;

    //null = no leido por el destinatario
    private LocalDateTime leido;
}
