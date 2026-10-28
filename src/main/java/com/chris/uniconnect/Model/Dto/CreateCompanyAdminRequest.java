package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//la contrasena no viene aqui: el sistema la genera y solo se envia por correo
public record CreateCompanyAdminRequest(
        @NotBlank(message = "El username es obligatorio") String username,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es valido") String email,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        String puesto
) {
}
