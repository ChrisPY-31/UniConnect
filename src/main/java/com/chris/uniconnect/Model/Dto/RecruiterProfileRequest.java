package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

//campos que el reclutador puede editar de su perfil; convenio, cuenta e imagen van por otros endpoints
public record RecruiterProfileRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        String puesto,
        String descripcion,
        String especialidad,
        String ciudad,
        Integer idUbicacion,
        @Past(message = "La fecha de nacimiento debe ser en el pasado") LocalDate fechaNacimiento
) {
}
