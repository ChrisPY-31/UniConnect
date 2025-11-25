package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Dto.Response.PersonaResponseM;
import com.chris.uniconnect.Model.Entity.PublicationInteractionPK;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationInteractionDto {

    private PublicationInteractionPK id;

    private Boolean meGusta;

    private String comentario;

    private LocalDateTime createdAt = LocalDateTime.now();


}
