package com.chris.uniconnect.Model.Dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class TechnologyDto {

    private Integer idTecnologia;

    @Column(name = "id_project")
    private Integer idProyecto;

    @Column(length = 100)
    private String nombre;


}
