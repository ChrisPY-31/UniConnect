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
    @Mapping(source = "person", target = "persona")
    PublicationInteractionDto entityToPublicationInteractionDto(PublicationInteraction publicationInteraction);


    @InheritInverseConfiguration
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "publication", ignore = true)
    PublicationInteraction DtoToPublicationInteraction(PublicationInteractionDto publicationInteractionDto);

    List<PublicationInteractionDto> entityToPublicationInteractionDtoList(List<PublicationInteraction> publicationInteractions);

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
