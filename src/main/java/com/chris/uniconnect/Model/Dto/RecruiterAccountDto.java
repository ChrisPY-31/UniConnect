package com.chris.uniconnect.Model.Dto;

import lombok.*;

//vista de cuenta (para quien administra reclutadores), sin el perfil completo de PersonDto
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterAccountDto {

    private Integer id;

    private Long idCorporacion;

    private String username;

    private String email;

    private String nombre;

    private String apellido;

    private String puesto;

    private Boolean cuentaActiva;
}
