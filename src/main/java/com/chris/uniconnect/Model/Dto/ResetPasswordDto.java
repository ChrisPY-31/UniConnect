package com.chris.uniconnect.Model.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {

    @NotBlank(message = "La nueva contrasena es obligatoria")
    @Size(min = 8, message = "La nueva contrasena debe tener al menos 8 caracteres")
    private String newPassword;
}
