package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.MessageDto;
import com.chris.uniconnect.Model.Entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//ConversationDto se arma en el servicio: contraparte y no leidos dependen de quien consulta
@Mapper
public interface ChatMappers {

    ChatMappers INSTANCE = Mappers.getMapper(ChatMappers.class);

    @Mapping(source = "idMessage", target = "idMensaje")
    @Mapping(source = "idConversation", target = "idConversacion")
    @Mapping(source = "idSender", target = "idRemitente")
    @Mapping(source = "senderType", target = "remitente")
    @Mapping(source = "content", target = "contenido")
    @Mapping(source = "sentAt", target = "enviado")
    @Mapping(source = "readAt", target = "leido")
    MessageDto toMessageDto(Message message);
}
