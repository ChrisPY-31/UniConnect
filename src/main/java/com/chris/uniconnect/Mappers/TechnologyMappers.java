package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.TechnologyDto;
import com.chris.uniconnect.Model.Entity.Technology;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TechnologyMappers {

    TechnologyMappers INSTANCE = Mappers.getMapper(TechnologyMappers.class);

    @Mapping(source = "idTechnology", target = "idTecnologia")
    @Mapping(source = "idProject", target = "idProyecto")
    @Mapping(source = "name", target = "nombre")
    TechnologyDto tecnologyToDto(Technology technology);

    @InheritInverseConfiguration
    Technology DtoToTechnology(TechnologyDto technologyDto);

    List<Technology> listTechnologyDtoToListTechnonlogy(List<TechnologyDto> technologiesDto);

    List<TechnologyDto> listTechnologyToListTechnonlogyDto(List<Technology> technologies);
}
