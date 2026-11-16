package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.IssuerType;
import com.chris.uniconnect.Enum.MessageEvent;
import lombok.*;

import java.time.OffsetDateTime;

//las fechas salen con zona (ej. 2026-11-08T23:09:33-06:00) para que el navegador no dependa de la zona del servidor
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long idMensaje;

    private Long idConversacion;

    private Integer idRemitente;

    private IssuerType remitente;

    //siempre null si el mensaje fue eliminado
    private String contenido;

    private OffsetDateTime enviado;

    //null = no leido por el destinatario
    private OffsetDateTime leido;

    //null = nunca editado; si trae fecha el front muestra "(editado)"
    private OffsetDateTime editado;

    //true = mostrar "Este mensaje fue eliminado"
    private boolean eliminado;

    //hasta cuando puede editarlo su autor (enviado + 15 min); null si esta eliminado
    private OffsetDateTime editableHasta;

    //si todavia se puede editar al momento de la respuesta (calculado con el reloj del servidor)
    private boolean editable;

    //que paso: NUEVO, EDITADO o ELIMINADO. Viene en lo que se empuja por WebSocket y en la respuesta de enviar/editar/eliminar;
    //null en el historial y en ultimoMensaje
    private MessageEvent evento;
}
