package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

//logo y banner tienen su propio endpoint
public record InstitutionProfileRequest(
        @NotBlank(message = "El nombre de la institucion es obligatorio") @Size(max = 150) String nombre,
        @Size(max = 3000, message = "La descripcion no puede pasar de 3000 caracteres") String descripcion,
        @URL(message = "El sitio web debe ser una URL valida") @Size(max = 255) String sitioWeb,
        @Size(max = 255) String ubicacion,
        @Email(message = "El correo de contacto no es valido") @Size(max = 255) String correoContacto
) {
}
