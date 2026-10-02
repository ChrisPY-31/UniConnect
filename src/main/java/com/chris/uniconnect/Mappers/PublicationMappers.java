package com.chris.uniconnect.Mappers;


import com.chris.uniconnect.Model.Dto.*;
import com.chris.uniconnect.Model.Dto.Response.PersonaResponseM;
import com.chris.uniconnect.Model.Dto.Response.RecruiterResponse;
import com.chris.uniconnect.Model.Dto.Response.StudentResponse;
import com.chris.uniconnect.Model.Dto.Response.TeacherResponse;
import com.chris.uniconnect.Model.Entity.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        PublicationInteractionMappers.class,
})
public interface PublicationMappers {

    PublicationMappers INSTANCE = Mappers.getMapper(PublicationMappers.class);

    @Mapping(source = "idPublication", target = "id")
    @Mapping(source = "idPerson", target = "idPersona")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "image", target = "imagen")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "person", target = "persona")
    @Mapping(source = "publicationInteractions", target = "interacciones")
    PublicationDto publicationDtoToPublicacionDto(Publication publication);


    @Mapping(source = "id", target = "idPublication")
    @Mapping(source = "idPersona", target = "idPerson")
    @Mapping(source = "descripcion", target = "description")
    @Mapping(source = "imagen", target = "image")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "persona", target = "person" , ignore = true)
    @Mapping(source = "interacciones", target = "publicationInteractions")
    Publication publicacionDtoToPublication(PublicationDto publicationDto);

    @InheritConfiguration(name = "publicacionDtoToPublication")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePublicationFromDto(PublicationDto publicationDto, @MappingTarget Publication publication);

    List<PublicationDto> publicationListToPublicacionDtoList(List<Publication> publicationList);
}
