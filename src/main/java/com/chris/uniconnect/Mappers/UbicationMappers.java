package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.UbicationDto;
import com.chris.uniconnect.Model.Entity.Ubication;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UbicationMappers {

    UbicationMappers INSTANCE = Mappers.getMapper(UbicationMappers.class);

    @Mapping(source = "idUbication", target = "idUbicacion")
    @Mapping(source = "state", target = "estado")
    UbicationDto ubicationToDto(Ubication ubication);

    @InheritInverseConfiguration
    Ubication dtoToUbication(UbicationDto ubicationDto);

    List<UbicationDto> listUbicationToListDto(List<Ubication> ubications);

    List<Ubication> listDtoToListUbication(List<UbicationDto> ubicationDtos);
}
