package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.MessageDto;
import com.chris.uniconnect.Model.Entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

//ConversationDto se arma en el servicio: contraparte y no leidos dependen de quien consulta
@Mapper(imports = {LocalDateTime.class, Message.class})
public interface ChatMappers {

    ChatMappers INSTANCE = Mappers.getMapper(ChatMappers.class);

    @Mapping(source = "idMessage", target = "idMensaje")
    @Mapping(source = "idConversation", target = "idConversacion")
    @Mapping(source = "idSender", target = "idRemitente")
    @Mapping(source = "senderType", target = "remitente")
    //un mensaje eliminado nunca expone su contenido (historial, ultimoMensaje y WebSocket pasan todos por aqui)
    @Mapping(target = "contenido", expression = "java(message.isDeleted() ? null : message.getContent())")
    @Mapping(source = "sentAt", target = "enviado")
    @Mapping(source = "readAt", target = "leido")
    @Mapping(source = "editedAt", target = "editado")
    @Mapping(source = "deleted", target = "eliminado")
    @Mapping(target = "editableHasta", expression = "java(message.isDeleted() ? null : toOffset(message.getSentAt().plus(Message.EDIT_WINDOW)))")
    @Mapping(target = "editable", expression = "java(!message.isDeleted() && LocalDateTime.now().isBefore(message.getSentAt().plus(Message.EDIT_WINDOW)))")
    @Mapping(target = "evento", ignore = true)
    MessageDto toMessageDto(Message message);

    //en BD las fechas se guardan como LocalDateTime con la zona del servidor; se responden con su desfase
    default OffsetDateTime toOffset(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }
}
