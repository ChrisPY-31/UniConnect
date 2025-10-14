package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.UbicationDto;
import com.chris.uniconnect.Model.Entity.Ubication;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UbicationMappers {

    UbicationMappers INSTANCE = Mappers.getMapper(UbicationMappers.class);

    @Mapping(source = "idUbication" , target = "idUbicacion")
    @Mapping(source = "country" , target = "pais")
    @Mapping(source = "city" , target = "ciudad")
    UbicationDto UbicationToUbicationDto(Ubication ubication);

    @InheritInverseConfiguration
    Ubication UbicationDtoToUbication(UbicationDto ubicationDto);
}
