package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.ConversationDto;
import com.chris.uniconnect.Model.Dto.MessageDto;
import com.chris.uniconnect.Model.Dto.SendMessageRequest;
import com.chris.uniconnect.Model.Dto.StartConversationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IChatService {

    //RECRUITER: abre (o reutiliza) la conversacion con un postulante a una de sus vacantes
    ConversationDto startConversation(String recruiterUsername, StartConversationRequest request);

    //STUDENT o RECRUITER: siempre como participante de la conversacion
    List<ConversationDto> getMyConversations(String username);

    ConversationDto getConversation(String username, Long idConversation);

    Page<MessageDto> getMessages(String username, Long idConversation, Pageable pageable);

    MessageDto sendMessage(String username, Long idConversation, SendMessageRequest request);

    ConversationDto markAsRead(String username, Long idConversation);

    //solo el estudiante
    ConversationDto changeBlock(String studentUsername, Long idConversation, boolean blocked);
}
