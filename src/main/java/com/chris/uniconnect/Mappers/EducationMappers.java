package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.EducationDto;
import com.chris.uniconnect.Model.Dto.Response.EducationResponse;
import com.chris.uniconnect.Model.Entity.Education;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EducationMappers {

    EducationMappers INSTANCE = Mappers.getMapper(EducationMappers.class);

    @Mapping(source = "idEducation", target = "idEducacion")
    @Mapping(source = "idPerson", target = "idPersona")
    @Mapping(source = "institution", target = "institucion")
    @Mapping(source = "degree", target = "grado")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "educationType", target = "educacionTipo")
    @Mapping(source = "startDate", target = "fechaInicio")
    @Mapping(source = "endDate", target = "fechaFin")
    EducationDto educationToEducationDto(Education education);

    @InheritInverseConfiguration

    Education educationDtoToEducation(EducationDto educationDto);

    EducationDto toDto(EducationResponse educationResponse);

    EducationResponse toEntity(EducationDto educationDto);

}
