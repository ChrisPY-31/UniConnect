package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Dto.Response.PersonaResponseM;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PublicationDto {

    private Integer id;

    @Column(name = "id_person")
    private Integer idPersona;

    private String descripcion;

    private String imagen;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    private PersonaResponseM persona;

    private List<PublicationInteractionDto> interacciones;


}
