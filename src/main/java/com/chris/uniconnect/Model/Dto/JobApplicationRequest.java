package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.Size;

//el CV no viene aqui: se copia el del perfil del estudiante
public record JobApplicationRequest(
        @Size(max = 2000, message = "El mensaje no puede pasar de 2000 caracteres") String mensaje
) {
}
