package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Model.Entity.Teacher;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        IdiomasMappers.class,
        UbicationMappers.class,
        EducationMappers.class,
        AptitudeMappers.class,
        PersonContactMappers.class
})
public interface TeacherMappers  {

    TeacherMappers INSTANCE = Mappers.getMapper(TeacherMappers.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "idUbication", target = "idUbicacion")
    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "lastName", target = "apellido")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "birthDate", target = "fechaNacimiento")
    @Mapping(source = "image", target = "imagen")
    @Mapping(source = "specialty", target = "especialidad")
    @Mapping(source = "resumeUrl", target = "curriculum")
    @Mapping(source = "aptitudes", target = "aptitudes")
    @Mapping(source = "personContacts", target = "redContactos")
    @Mapping(source = "educations", target = "educaciones")
    @Mapping(source = "ubication", target = "ubicacion")
    @Mapping(source = "languages", target = "idiomas")

    TeacherDto teacherToTeacherDto(Teacher teacher);

    @InheritInverseConfiguration
    Teacher teacherDtoToTeacher(TeacherDto teacherDto);

}
