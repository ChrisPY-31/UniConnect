package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Entity.PublicationInteractionPK;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublicationInteractionDto {

    private PublicationInteractionPK id;

    private Boolean meGusta;

    private String comentario;

    private LocalDateTime createdAt = LocalDateTime.now();


}
