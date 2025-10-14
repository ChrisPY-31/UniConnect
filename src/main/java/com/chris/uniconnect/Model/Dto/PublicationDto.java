package com.chris.uniconnect.Model.Dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublicationDto {

    private Integer id;

    @Column(name = "id_person")
    private Integer idPersona;

    private String descripcion;

    private String imagen;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
