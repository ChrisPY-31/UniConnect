package com.chris.uniconnect.Service;

import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Model.Dto.Response.NotificationResponse;
import com.chris.uniconnect.Model.Entity.RecomendationPk;

import java.util.List;

public interface INotificationService {

    //todas por JWT: la persona sale del username, nunca del cliente
    List<NotificationResponse> getMyNotifications(String username);

    //ruta antigua /notifications/{personId}: solo responde si personId es el del usuario autenticado
    List<NotificationResponse> getNotifications(String username, Integer personId);

    void notifyRecommendation(RecomendationPk id);

    void notifyProjectMention(Integer studentId, Integer projectId, String projectName);

    //al reclutador dueno de la vacante; referenceId = id de la vacante
    void notifyJobApplicationReceived(Integer recruiterId, Long idPostJob, String studentName, String jobTitle);

    //al estudiante; referenceId = id de la postulacion
    void notifyJobApplicationStatus(Integer studentId, Long idJobApplication, String jobTitle, AplicationsStatus status);

    NotificationResponse markAsRead(String username, Integer id);

    void deleteMine(String username, Integer id);

    //ids ajenos o inexistentes se ignoran; regresa cuantas se borraron
    int deleteMine(String username, List<Integer> ids);

    int deleteAllMine(String username, boolean onlyRead);

    //tarea diaria: leidas con mas de N dias
    int deleteReadOlderThan(int days);
}
