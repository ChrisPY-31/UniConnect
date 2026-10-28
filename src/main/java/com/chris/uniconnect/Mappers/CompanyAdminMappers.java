package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.CompanyAdminDto;
import com.chris.uniconnect.Model.Entity.CompanyAdmin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        IdiomasMappers.class,
        UbicationMappers.class,
        EducationMappers.class,
        AptitudeMappers.class,
        SkillMappers.class,
        PersonContactMappers.class,
        CorporatePartnershipsMappers.class
})
public interface CompanyAdminMappers {

    CompanyAdminMappers INSTANCE = Mappers.getMapper(CompanyAdminMappers.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "idUbication", target = "idUbicacion")
    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "lastName", target = "apellido")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "birthDate", target = "fechaNacimiento")
    @Mapping(source = "image", target = "imagen")
    @Mapping(source = "specialty", target = "especialidad")
    @Mapping(source = "resumeUrl", target = "curriculum")
    @Mapping(source = "type", target = "tipo")
    @Mapping(source = "aptitudes", target = "aptitudes")
    @Mapping(source = ".", target = "habilidades")
    @Mapping(source = "personContacts", target = "redContactos")
    @Mapping(source = "educations", target = "educaciones")
    @Mapping(source = "ubication", target = "ubicacion")
    @Mapping(source = "city", target = "ciudad")
    @Mapping(source = "languages", target = "lenguajes")
    @Mapping(source = "idCorporatePartnership", target = "idCorporacion")
    @Mapping(source = "position", target = "puesto")
    @Mapping(source = "userEntity.username", target = "username")
    @Mapping(source = "userEntity.email", target = "email")
    @Mapping(source = "userEntity.enabled", target = "cuentaActiva")
    @Mapping(source = "corporatePartnershipsUAEMex", target = "empresa")
    CompanyAdminDto toDto(CompanyAdmin companyAdmin);

    List<CompanyAdminDto> toDtoList(List<CompanyAdmin> companyAdmins);
}
