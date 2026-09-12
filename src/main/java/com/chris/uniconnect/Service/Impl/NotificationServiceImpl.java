package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Enum.NotificationType;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Model.Dto.Response.NotificationResponse;
import com.chris.uniconnect.Model.Entity.Notification;
import com.chris.uniconnect.Model.Entity.RecomendationPk;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Repository.NotificationRepository;
import com.chris.uniconnect.Repository.PersonaRepository;
import com.chris.uniconnect.Repository.TeacherRepostory;
import com.chris.uniconnect.Repository.UserRepository;
import com.chris.uniconnect.Service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TeacherRepostory teacherRepostory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public List<NotificationResponse> getNotifications(Integer personId) {
        return notificationRepository.findByPerson_IdOrderByCreatedAtDesc(personId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void notifyRecommendation(RecomendationPk id) {
        Teacher teacher = teacherRepostory.findById(id.getIdTeacher())
                .orElseThrow(() -> new ResourceNotFoundException("teacher", "id", id.getIdTeacher()));

        String teacherName = teacher.getName() + " " + teacher.getLastName();

        Notification notification = Notification.builder()
                .person(personaRepository.getReferenceById(id.getIdStudent()))
                .type(NotificationType.RECOMMENDATION)
                .message("El maestro " + teacherName + " te dejó una recomendación")
                .referenceId(id.getIdTeacher())
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        pushToPerson(id.getIdStudent(), notification);
    }

    @Override
    public void notifyProjectMention(Integer studentId, Integer projectId, String projectName) {
        Notification notification = Notification.builder()
                .person(personaRepository.getReferenceById(studentId))
                .type(NotificationType.PROJECT_MENTION)
                .message("Fuiste mencionado en el proyecto " + projectName)
                .referenceId(projectId)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        pushToPerson(studentId, notification);
    }

    private void pushToPerson(Integer personId, Notification notification) {
        userRepository.findByPersonId(personId).ifPresent(user ->
                messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/notifications", toResponse(notification))
        );
    }

    @Override
    public NotificationResponse markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("notification", "id", id));

        notification.setRead(true);

        return toResponse(notificationRepository.save(notification));
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getReferenceId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
