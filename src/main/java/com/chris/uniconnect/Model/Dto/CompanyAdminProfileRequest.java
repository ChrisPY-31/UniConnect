package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyAdminProfileRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        String puesto,
        String descripcion
) {
}
