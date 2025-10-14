package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.PublicationInteractionDto;
import com.chris.uniconnect.Model.Entity.PublicationInteraction;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = PublicationMappers.class)
public interface PublicationInteractionMappers {

    PublicationInteractionMappers INSTANCE = Mappers.getMapper(PublicationInteractionMappers.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "liked" , target = "meGusta")
    @Mapping(source = "comment" , target = "comentario")
    @Mapping(source = "createdAt" , target = "createdAt")
    @Mapping(source = "publication" , target = "publicacion")
    PublicationInteractionDto entityToPublicationInteractionDto(PublicationInteraction publicationInteraction);

    @InheritInverseConfiguration
    PublicationInteraction DtoToPublicationInteraction(PublicationInteractionDto publicationInteractionDto);

    List<PublicationInteractionDto> entityToPublicationInteractionDtoList(List<PublicationInteraction> publicationInteractions);
}
