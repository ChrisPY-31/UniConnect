package com.chris.uniconnect.Mappers;


import com.chris.uniconnect.Model.Dto.PublicationDto;
import com.chris.uniconnect.Model.Entity.Publication;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = PublicationInteractionMappers.class)
public interface PublicationMappers {

    PublicationMappers INSTANCE = Mappers.getMapper(PublicationMappers.class);

    @Mapping(source = "idPublication" , target = "id")
    @Mapping(source = "idPerson" , target = "idPersona")
    @Mapping(source = "description" , target = "descripcion")
    @Mapping(source = "image" , target = "imagen")
    @Mapping(source = "createdAt" , target = "createdAt")
    @Mapping(source = "publicationInteractions" , target = "interacciones")

    PublicationDto publicationDtoToPublicacionDto(Publication publication);

    @InheritInverseConfiguration

    Publication publicacionDtoToPublication(PublicationDto publicationDto);

    List<Publication> publicacionDtoToPublicationList(List<PublicationDto> publicationDto);

    List<PublicationDto> publicationListToPublicacionDtoList(List<Publication> publicationList);


}
