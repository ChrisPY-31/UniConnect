package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Enum.NotificationType;
import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Model.Dto.Response.NotificationResponse;
import com.chris.uniconnect.Model.Entity.Notification;
import com.chris.uniconnect.Model.Entity.Person;
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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(String username) {
        return notificationRepository.findByPerson_IdOrderByCreatedAtDesc(myPersonId(username)).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(String username, Integer personId) {
        if (!myPersonId(username).equals(personId)) {
            throw new BadRequestException("Solo puedes consultar tus propias notificaciones");
        }
        return getMyNotifications(username);
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

    @Override
    public void notifyJobApplicationReceived(Integer recruiterId, Long idPostJob, String studentName, String jobTitle) {
        Notification notification = Notification.builder()
                .person(personaRepository.getReferenceById(recruiterId))
                .type(NotificationType.JOB_APPLICATION_RECEIVED)
                .message(studentName + " se postulo a tu vacante " + jobTitle)
                .referenceId(Math.toIntExact(idPostJob))
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        pushToPerson(recruiterId, notification);
    }

    @Override
    public void notifyJobApplicationStatus(Integer studentId, Long idJobApplication, String jobTitle, AplicationsStatus status) {
        String statusText = switch (status) {
            case EN_REVISION -> "esta en revision";
            case ENTREVISTA -> "paso a entrevista";
            case ACEPTADA -> "fue aceptada";
            case RECHAZADA -> "no fue seleccionada";
            default -> "cambio de estado";
        };

        Notification notification = Notification.builder()
                .person(personaRepository.getReferenceById(studentId))
                .type(NotificationType.JOB_APPLICATION_STATUS)
                .message("Tu postulacion a " + jobTitle + " " + statusText)
                .referenceId(Math.toIntExact(idJobApplication))
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
    @Transactional
    public NotificationResponse markAsRead(String username, Integer id) {
        Notification notification = findMine(username, id);
        notification.setRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void deleteMine(String username, Integer id) {
        notificationRepository.delete(findMine(username, id));
    }

    @Override
    @Transactional
    public int deleteMine(String username, List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return notificationRepository.deleteByOwnerAndIds(myPersonId(username), ids);
    }

    @Override
    @Transactional
    public int deleteAllMine(String username, boolean onlyRead) {
        Integer personId = myPersonId(username);
        return onlyRead
                ? notificationRepository.deleteReadByOwner(personId)
                : notificationRepository.deleteAllByOwner(personId);
    }

    @Override
    @Transactional
    public int deleteReadOlderThan(int days) {
        return notificationRepository.deleteReadOlderThan(LocalDateTime.now().minusDays(days));
    }

    //una notificacion ajena responde igual que una inexistente
    private Notification findMine(String username, Integer id) {
        return notificationRepository.findByIdAndPerson_Id(id, myPersonId(username))
                .orElseThrow(() -> new ResourceNotFoundException("notification", "id", id));
    }

    //sirve para todos los roles con Person (estudiante, profesor, reclutador, empresa, admin)
    private Integer myPersonId(String username) {
        Person person = personaRepository.findByUserEntityUsername(username);
        if (person == null) {
            throw new ResourceNotFoundException("Persona", "username", username);
        }
        return person.getId();
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
