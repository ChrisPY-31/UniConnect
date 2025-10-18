package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.*;
import com.chris.uniconnect.Model.Entity.*;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        IdiomasMappers.class,
        UbicationMappers.class,
        EducationMappers.class,
        AptitudeMappers.class,
        CareerMappers.class,
        PersonContactMappers.class,
        ProjectMappers.class,
        RecomendationMappers.class
})
public interface PersonMappers {

    PersonMappers INSTANCE = Mappers.getMapper(PersonMappers.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "idCareer", target = "idCarrera")
    @Mapping(source = "idUbication", target = "idUbicacion")
    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "lastName", target = "apellido")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "birthDate", target = "fechaNacimiento")
    @Mapping(source = "image", target = "imagen")
    @Mapping(source = "specialty", target = "especialidad")
    @Mapping(source = "resumeUrl", target = "curriculum")
    @Mapping(source = "aptitudes", target = "aptitudes")
    @Mapping(source = "semester", target = "semestre")
    @Mapping(source = "career", target = "carrera")
    @Mapping(source = "personContacts", target = "redContactos")
    @Mapping(source = "educations", target = "educaciones")
    @Mapping(source = "ubication", target = "ubicacion")
    @Mapping(source = "languages", target = "idiomas")
    @Mapping(source = "projects", target = "proyectos")
    StudentDto studentToStudentDto(Student student);

    @InheritInverseConfiguration
    Student studentDtoToStudent(StudentDto studentDto);
}
