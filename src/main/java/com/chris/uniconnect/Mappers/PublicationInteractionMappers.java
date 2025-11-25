package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.PublicationInteractionDto;
import com.chris.uniconnect.Model.Dto.Response.PersonaResponseM;
import com.chris.uniconnect.Model.Dto.Response.RecruiterResponse;
import com.chris.uniconnect.Model.Dto.Response.StudentResponse;
import com.chris.uniconnect.Model.Dto.Response.TeacherResponse;
import com.chris.uniconnect.Model.Entity.*;
import jakarta.persistence.MappedSuperclass;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PublicationInteractionMappers {

    PublicationInteractionMappers INSTANCE = Mappers.getMapper(PublicationInteractionMappers.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "liked", target = "meGusta")
    @Mapping(source = "comment", target = "comentario")
    PublicationInteractionDto entityToPublicationInteractionDto(PublicationInteraction publicationInteraction);


    @InheritInverseConfiguration
    PublicationInteraction DtoToPublicationInteraction(PublicationInteractionDto publicationInteractionDto);

    List<PublicationInteractionDto> entityToPublicationInteractionDtoList(List<PublicationInteraction> publicationInteractions);


}
