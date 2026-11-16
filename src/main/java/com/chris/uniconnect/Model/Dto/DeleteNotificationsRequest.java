package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DeleteNotificationsRequest(
        @NotEmpty(message = "Manda al menos un id") @Size(max = 200, message = "Maximo 200 notificaciones por peticion") List<Integer> ids
) {
}
