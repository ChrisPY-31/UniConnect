package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
        @NotBlank(message = "El mensaje es obligatorio") @Size(max = 2000, message = "El mensaje no puede pasar de 2000 caracteres") String contenido
) {
}
