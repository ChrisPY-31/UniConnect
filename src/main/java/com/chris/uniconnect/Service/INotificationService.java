package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.Response.NotificationResponse;
import com.chris.uniconnect.Model.Entity.RecomendationPk;

import java.util.List;

public interface INotificationService {

    List<NotificationResponse> getNotifications(Integer personId);

    void notifyRecommendation(RecomendationPk id);

    void notifyProjectMention(Integer studentId, Integer projectId, String projectName);

    NotificationResponse markAsRead(Integer id);
}
