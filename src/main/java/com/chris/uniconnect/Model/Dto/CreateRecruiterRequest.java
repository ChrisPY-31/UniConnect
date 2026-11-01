package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//el convenio no viene aqui: se toma del COMPANY_ADMIN autenticado; la contrasena se genera y se envia por correo
public record CreateRecruiterRequest(
        @NotBlank(message = "El username es obligatorio") String username,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es valido") String email,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        String puesto
) {
}
