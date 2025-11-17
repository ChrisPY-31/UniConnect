package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Model.Entity.Recruiter;
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
        PersonContactMappers.class,
        CompanyMappers.class
})
public interface RecruiterMappers {

    RecruiterMappers INSTANCE = Mappers.getMapper(RecruiterMappers.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "idUbication", target = "idUbicacion")
    @Mapping(source = "idCompany" , target ="idCompania")
    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "lastName", target = "apellido")
    @Mapping(source = "position", target = "posicion")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "birthDate", target = "fechaNacimiento")
    @Mapping(source = "image", target = "imagen")
    @Mapping(source = "specialty", target = "especialidad")
    @Mapping(source = "resumeUrl", target = "curriculum")
    @Mapping(source = "type", target = "tipo")
    @Mapping(source = "aptitudes", target = "aptitudes")
    @Mapping(source = "personContacts", target = "redContactos")
    @Mapping(source = "educations", target = "educaciones")
    @Mapping(source = "ubication", target = "ubicacion")
    @Mapping(source = "languages", target = "lenguajes")
    @Mapping(source = "company", target = "compania")
    RecruiterDto recruiterToRecruiterDto(Recruiter recruiter);

    @InheritInverseConfiguration
    Recruiter recruiterDtoToRecruiter(RecruiterDto recruiterDto);
}
