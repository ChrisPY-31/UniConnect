package com.chris.uniconnect.Model.Dto;

import lombok.*;

//datos del reclutador que se muestran en una vacante
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRecruiterDto {

    private Integer id;

    private String nombre;

    private String apellido;

    private String puesto;

    private String imagen;
}
