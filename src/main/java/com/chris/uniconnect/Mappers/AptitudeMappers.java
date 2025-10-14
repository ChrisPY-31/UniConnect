package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.AptitudeDto;
import com.chris.uniconnect.Model.Dto.Response.AptitudeResponse;
import com.chris.uniconnect.Model.Entity.Aptitude;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AptitudeMappers {

    AptitudeMappers INSTANCE = Mappers.getMapper(AptitudeMappers.class);

    @Mapping(source = "idAptitude", target = "idAptitud")
    @Mapping(source = "idPerson", target = "idPersona")
    @Mapping(source = "name", target = "nombre")
    AptitudeDto aptitudeToAptitudeDto(Aptitude aptitude);

    @InheritInverseConfiguration
    Aptitude aptitudeDtoToAptitude(AptitudeDto aptitudeDto);

    List<AptitudeDto> toEntity(List<Aptitude> aptitudes);

    List<Aptitude> aptitudeToEntity(List<AptitudeDto> aptitudeDtos);

    AptitudeDto toDto(AptitudeResponse aptitude);

    AptitudeResponse toResponse(AptitudeDto aptitude);
}

