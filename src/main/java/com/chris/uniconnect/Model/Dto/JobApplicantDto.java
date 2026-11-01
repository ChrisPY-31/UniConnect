package com.chris.uniconnect.Model.Dto;

import lombok.*;

//datos del estudiante que ve el reclutador en una postulacion; el perfil completo esta en GET /person/{id}
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicantDto {

    private Integer id;

    private String nombre;

    private String apellido;

    private String imagen;

    private String carrera;

    private String semestre;
}
