package com.chris.uniconnect.Mappers;


import com.chris.uniconnect.Model.Dto.*;
import com.chris.uniconnect.Model.Dto.Response.PersonaResponseM;
import com.chris.uniconnect.Model.Dto.Response.RecruiterResponse;
import com.chris.uniconnect.Model.Dto.Response.StudentResponse;
import com.chris.uniconnect.Model.Dto.Response.TeacherResponse;
import com.chris.uniconnect.Model.Entity.*;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    List<PublicationDto> publicationListToPublicacionDtoList(List<Publication> publicationList);


    default PersonaResponseM map(Person persona) {
        if (persona instanceof Teacher teacher) {
            TeacherResponse response = new TeacherResponse();
            response.setId(teacher.getId());
            response.setNombre(teacher.getName());
            response.setApellido(teacher.getLastName());
            response.setEspecialidad(teacher.getSpecialty());
            response.setImagen(teacher.getImage());
            return response;
        } else if (persona instanceof Recruiter recruiter) {
            RecruiterResponse response = new RecruiterResponse();
            response.setId(recruiter.getId());
            response.setNombre(recruiter.getName());
            response.setApellido(recruiter.getLastName());
            response.setEspecialidad(recruiter.getSpecialty());
            response.setImagen(recruiter.getImage());
            return response;
        } else if (persona instanceof Student student) {
            StudentResponse response = new StudentResponse();
            response.setId(student.getId());
            response.setNombre(student.getName());
            response.setApellido(student.getLastName());
            response.setEspecialidad(student.getSpecialty());
            response.setImagen(student.getImage());
            return response;
        }
        return null;
    }
}
