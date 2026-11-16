package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//el reclutador abre el chat directo con cualquier estudiante (no hace falta que se haya postulado), con un primer mensaje
public record StartConversationRequest(
        @NotNull(message = "El estudiante es obligatorio") Integer idEstudiante,
        @NotBlank(message = "El mensaje es obligatorio") @Size(max = 2000, message = "El mensaje no puede pasar de 2000 caracteres") String mensaje
) {
}
