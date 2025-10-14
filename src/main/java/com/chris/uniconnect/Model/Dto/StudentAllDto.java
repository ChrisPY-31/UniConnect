package com.chris.uniconnect.Model.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentAllDto {

    private Integer id;
    private String nombre;
    private String apellido;
    private String imagen;
    private CareerDto carrera;
}
