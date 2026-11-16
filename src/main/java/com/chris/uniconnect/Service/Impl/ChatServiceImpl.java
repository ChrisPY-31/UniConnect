package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Enum.ConversationStatus;
import com.chris.uniconnect.Enum.IssuerType;
import com.chris.uniconnect.Enum.MessageEvent;
import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.ChatMappers;
import com.chris.uniconnect.Mappers.CorporatePartnershipsMappers;
import com.chris.uniconnect.Mappers.JobApplicationMappers;
import com.chris.uniconnect.Model.Dto.*;
import com.chris.uniconnect.Model.Entity.*;
import com.chris.uniconnect.Repository.*;
import com.chris.uniconnect.Service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements IChatService {

    private static final String MESSAGES_QUEUE = "/queue/messages";

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //quien hace la peticion dentro del chat
    private record Participant(Integer id, IssuerType type) {
    }

    @Override
    @Transactional
    public ConversationDto startConversation(String recruiterUsername, StartConversationRequest request) {
        Recruiter recruiter = recruiterRepository.findByUserEntityUsername(recruiterUsername);
        if (recruiter == null) {
            throw new ResourceNotFoundException("Reclutador", "username", recruiterUsername);
        }
        //chat directo: no hace falta postulacion; una cuenta de estudiante deshabilitada responde como inexistente
        Student student = studentRepository.findById(request.idEstudiante())
                .filter(found -> found.getUserEntity() != null && found.getUserEntity().isEnabled())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "id", request.idEstudiante()));

        //si ya existe la conversacion con este estudiante se reutiliza (si el la bloqueo, el envio falla)
        Conversation conversation = conversationRepository
                .findByIdStudentAndIdRecruiter(student.getId(), recruiter.getId())
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setIdStudent(student.getId());
                    newConversation.setStudent(student);
                    newConversation.setIdRecruiter(recruiter.getId());
                    newConversation.setRecruiter(recruiter);
                    newConversation.setStatus(ConversationStatus.ACTIVA);
                    newConversation.setCreatedAt(LocalDateTime.now());
                    return conversationRepository.save(newConversation);
                });

        Participant me = new Participant(recruiter.getId(), IssuerType.RECLUTADOR);
        saveAndPushMessage(conversation, me, request.mensaje());
        return toConversationDto(conversation, me, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationDto> getMyConversations(String username) {
        Participant me = findParticipant(username);
        List<Conversation> conversations = me.type() == IssuerType.ALUMNO
                ? conversationRepository.findByIdStudentOrderByLastMessageAtDesc(me.id())
                : conversationRepository.findByIdRecruiterOrderByLastMessageAtDesc(me.id());
        if (conversations.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> unread = new HashMap<>();
        List<Long> ids = conversations.stream().map(Conversation::getIdConversation).toList();
        for (Object[] row : messageRepository.countUnreadByConversation(ids, me.type())) {
            unread.put((Long) row[0], (Long) row[1]);
        }
        return conversations.stream()
                .map(conversation -> toConversationDto(conversation, me, unread.getOrDefault(conversation.getIdConversation(), 0L)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationDto getConversation(String username, Long idConversation) {
        Participant me = findParticipant(username);
        Conversation conversation = findMyConversation(me, idConversation);
        return toConversationDto(conversation, me, countUnread(conversation, me));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getMessages(String username, Long idConversation, Pageable pageable) {
        Conversation conversation = findMyConversation(findParticipant(username), idConversation);
        return messageRepository.findByIdConversationOrderBySentAtDesc(conversation.getIdConversation(), pageable)
                .map(ChatMappers.INSTANCE::toMessageDto);
    }

    @Override
    @Transactional
    public MessageDto sendMessage(String username, Long idConversation, SendMessageRequest request) {
        Participant me = findParticipant(username);
        Conversation conversation = findMyConversation(me, idConversation);
        return saveAndPushMessage(conversation, me, request.contenido());
    }

    @Override
    @Transactional
    public MessageDto editMessage(String username, Long idConversation, Long idMessage, SendMessageRequest request) {
        Participant me = findParticipant(username);
        Conversation conversation = findMyConversation(me, idConversation);
        Message message = findMyMessage(conversation, me, idMessage);
        if (message.isDeleted()) {
            throw new BadRequestException("El mensaje fue eliminado, ya no se puede editar");
        }
        if (message.getSentAt().plus(Message.EDIT_WINDOW).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Solo se puede editar un mensaje durante los primeros " + Message.EDIT_WINDOW.toMinutes() + " minutos");
        }

        //mismo texto: no cuenta como edicion ni se empuja por WebSocket
        String newContent = request.contenido().trim();
        if (newContent.equals(message.getContent())) {
            return ChatMappers.INSTANCE.toMessageDto(message);
        }

        message.setContent(newContent);
        message.setEditedAt(LocalDateTime.now());
        MessageDto messageDto = ChatMappers.INSTANCE.toMessageDto(messageRepository.save(message));
        messageDto.setEvento(MessageEvent.EDITADO);
        pushToParticipants(conversation, messageDto);
        return messageDto;
    }

    @Override
    @Transactional
    public MessageDto deleteMessage(String username, Long idConversation, Long idMessage) {
        Participant me = findParticipant(username);
        Conversation conversation = findMyConversation(me, idConversation);
        Message message = findMyMessage(conversation, me, idMessage);
        if (message.isDeleted()) {
            throw new BadRequestException("El mensaje ya estaba eliminado");
        }

        //se vacia el contenido en BD, no solo en la respuesta
        message.setDeleted(true);
        message.setContent("");
        MessageDto messageDto = ChatMappers.INSTANCE.toMessageDto(messageRepository.save(message));
        messageDto.setEvento(MessageEvent.ELIMINADO);
        pushToParticipants(conversation, messageDto);
        return messageDto;
    }

    //solo mensajes propios; uno ajeno responde igual que uno inexistente. Bloqueada = nadie envia ni modifica
    private Message findMyMessage(Conversation conversation, Participant me, Long idMessage) {
        if (conversation.getStatus() == ConversationStatus.BLOQUEADA) {
            throw new BadRequestException("La conversacion esta bloqueada, no se pueden modificar mensajes");
        }
        return messageRepository.findByIdMessageAndIdConversation(idMessage, conversation.getIdConversation())
                .filter(message -> message.getIdSender().equals(me.id()) && message.getSenderType() == me.type())
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje", "id", idMessage));
    }

    @Override
    @Transactional
    public ConversationDto markAsRead(String username, Long idConversation) {
        Participant me = findParticipant(username);
        Conversation conversation = findMyConversation(me, idConversation);
        messageRepository.markAsRead(conversation.getIdConversation(), me.type(), LocalDateTime.now());
        return toConversationDto(conversation, me, 0);
    }

    @Override
    @Transactional
    public ConversationDto changeBlock(String studentUsername, Long idConversation, boolean blocked) {
        Participant me = findParticipant(studentUsername);
        if (me.type() != IssuerType.ALUMNO) {
            throw new BadRequestException("Solo el estudiante puede bloquear o desbloquear la conversacion");
        }
        Conversation conversation = findMyConversation(me, idConversation);
        ConversationStatus target = blocked ? ConversationStatus.BLOQUEADA : ConversationStatus.ACTIVA;
        if (conversation.getStatus() == target) {
            throw new BadRequestException("La conversacion ya esta " + target);
        }
        conversation.setStatus(target);
        Conversation saved = conversationRepository.save(conversation);
        return toConversationDto(saved, me, countUnread(saved, me));
    }

    private MessageDto saveAndPushMessage(Conversation conversation, Participant sender, String content) {
        if (conversation.getStatus() == ConversationStatus.BLOQUEADA) {
            throw new BadRequestException("La conversacion esta bloqueada, no se pueden enviar mensajes");
        }
        LocalDateTime now = LocalDateTime.now();

        Message message = new Message();
        message.setIdConversation(conversation.getIdConversation());
        message.setIdSender(sender.id());
        message.setSenderType(sender.type());
        message.setContent(content.trim());
        message.setSentAt(now);
        MessageDto messageDto = ChatMappers.INSTANCE.toMessageDto(messageRepository.save(message));
        messageDto.setEvento(MessageEvent.NUEVO);

        conversation.setLastMessageAt(now);
        conversationRepository.save(conversation);

        pushToParticipants(conversation, messageDto);
        return messageDto;
    }

    //a los dos participantes (el remitente puede tener otra pestana abierta), ya confirmada la transaccion.
    //nuevo, editado o eliminado viajan por la misma cola; el campo evento dice cual fue (reemplazar por idMensaje si ya lo tiene)
    private void pushToParticipants(Conversation conversation, MessageDto messageDto) {
        String studentUsername = conversation.getStudent().getUserEntity().getUsername();
        String recruiterUsername = conversation.getRecruiter().getUserEntity().getUsername();
        afterCommit(() -> {
            messagingTemplate.convertAndSendToUser(studentUsername, MESSAGES_QUEUE, messageDto);
            messagingTemplate.convertAndSendToUser(recruiterUsername, MESSAGES_QUEUE, messageDto);
        });
    }

    private void afterCommit(Runnable action) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    action.run();
                }
            });
        } else {
            action.run();
        }
    }

    private ConversationDto toConversationDto(Conversation conversation, Participant me, long unread) {
        ConversationDto dto = new ConversationDto();
        dto.setIdConversacion(conversation.getIdConversation());
        dto.setEstado(conversation.getStatus());
        dto.setCreada(ChatMappers.INSTANCE.toOffset(conversation.getCreatedAt()));
        dto.setUltimoMensajeEn(ChatMappers.INSTANCE.toOffset(conversation.getLastMessageAt()));
        dto.setUltimoMensaje(messageRepository.findFirstByIdConversationOrderBySentAtDesc(conversation.getIdConversation())
                .map(ChatMappers.INSTANCE::toMessageDto)
                .orElse(null));
        dto.setNoLeidos(unread);
        if (conversation.getJobApplication() != null) {
            dto.setVacante(JobApplicationMappers.INSTANCE.toJobDto(conversation.getJobApplication().getPostJob()));
        }

        ChatParticipantDto other = new ChatParticipantDto();
        if (me.type() == IssuerType.ALUMNO) {
            Recruiter recruiter = conversation.getRecruiter();
            other.setId(recruiter.getId());
            other.setTipo(IssuerType.RECLUTADOR);
            other.setNombre(recruiter.getName());
            other.setApellido(recruiter.getLastName());
            other.setImagen(recruiter.getImage());
            other.setEmpresa(CorporatePartnershipsMappers.INSTANCE.toSummaryDto(recruiter.getCorporatePartnershipsUAEMex()));
        } else {
            Student student = conversation.getStudent();
            other.setId(student.getId());
            other.setTipo(IssuerType.ALUMNO);
            other.setNombre(student.getName());
            other.setApellido(student.getLastName());
            other.setImagen(student.getImage());
        }
        dto.setContraparte(other);
        return dto;
    }

    private long countUnread(Conversation conversation, Participant me) {
        return messageRepository.countUnreadByConversation(List.of(conversation.getIdConversation()), me.type()).stream()
                .mapToLong(row -> (Long) row[1])
                .sum();
    }

    private Participant findParticipant(String username) {
        Student student = studentRepository.findByUserEntityUsername(username);
        if (student != null) {
            return new Participant(student.getId(), IssuerType.ALUMNO);
        }
        Recruiter recruiter = recruiterRepository.findByUserEntityUsername(username);
        if (recruiter != null) {
            return new Participant(recruiter.getId(), IssuerType.RECLUTADOR);
        }
        throw new ResourceNotFoundException("Participante del chat", "username", username);
    }

    //404 tambien si existe pero no participas, para no revelar conversaciones ajenas
    private Conversation findMyConversation(Participant me, Long idConversation) {
        Conversation conversation = conversationRepository.findById(idConversation)
                .orElseThrow(() -> new ResourceNotFoundException("Conversacion", "id", idConversation));
        Integer participantId = me.type() == IssuerType.ALUMNO ? conversation.getIdStudent() : conversation.getIdRecruiter();
        if (!me.id().equals(participantId)) {
            throw new ResourceNotFoundException("Conversacion", "id", idConversation);
        }
        return conversation;
    }
}
