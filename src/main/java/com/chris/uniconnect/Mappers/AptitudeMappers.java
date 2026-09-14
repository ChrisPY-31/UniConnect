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
    @Mapping(source = "name", target = "nombre")
    AptitudeDto aptitudeToAptitudeDto(Aptitude aptitude);

    @InheritInverseConfiguration
    Aptitude aptitudeDtoToAptitude(AptitudeDto aptitudeDto);

    List<AptitudeDto> listAptitudeToListAptitudeDto(List<Aptitude> aptitudes);

    List<Aptitude> listAptitudeDtoToListAptitude(List<AptitudeDto> aptitudeDtos);

    @Mapping(source = "idAptitude", target = "idAptitud")
    @Mapping(source = "name", target = "nombre")
    AptitudeResponse aptitudeToResponse(Aptitude aptitude);
}
