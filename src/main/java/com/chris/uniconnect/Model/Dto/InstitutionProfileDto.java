package com.chris.uniconnect.Model.Dto;

import java.time.LocalDateTime;

public record InstitutionProfileDto(
        String nombre,
        String descripcion,
        String logo,
        String banner,
        String sitioWeb,
        String ubicacion,
        String correoContacto,
        LocalDateTime actualizado
) {
}
