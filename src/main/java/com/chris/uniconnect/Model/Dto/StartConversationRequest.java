package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//el reclutador abre el chat desde una postulacion a una de sus vacantes, con un primer mensaje
public record StartConversationRequest(
        @NotNull(message = "La postulacion es obligatoria") Long idPostulacion,
        @NotBlank(message = "El mensaje es obligatorio") @Size(max = 2000, message = "El mensaje no puede pasar de 2000 caracteres") String mensaje
) {
}
