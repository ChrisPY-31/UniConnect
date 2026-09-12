package com.chris.uniconnect.Model.Dto.Response;

import com.chris.uniconnect.Enum.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Integer id,
        NotificationType type,
        String message,
        Integer referenceId,
        boolean read,
        LocalDateTime createdAt
) {
}
