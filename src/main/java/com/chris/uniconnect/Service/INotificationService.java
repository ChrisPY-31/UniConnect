package com.chris.uniconnect.Service;

import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Model.Dto.Response.NotificationResponse;
import com.chris.uniconnect.Model.Entity.RecomendationPk;

import java.util.List;

public interface INotificationService {

    List<NotificationResponse> getNotifications(Integer personId);

    void notifyRecommendation(RecomendationPk id);

    void notifyProjectMention(Integer studentId, Integer projectId, String projectName);

    //al reclutador dueno de la vacante; referenceId = id de la vacante
    void notifyJobApplicationReceived(Integer recruiterId, Long idPostJob, String studentName, String jobTitle);

    //al estudiante; referenceId = id de la postulacion
    void notifyJobApplicationStatus(Integer studentId, Long idJobApplication, String jobTitle, AplicationsStatus status);

    NotificationResponse markAsRead(Integer id);
}
