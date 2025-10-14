package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.CareerDto;
import com.chris.uniconnect.Model.Entity.Career;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CareerMappers {

    CareerMappers INSTANCE = Mappers.getMapper(CareerMappers.class);

    @Mapping(source = "idCareer", target = "idCarrera")
    @Mapping(source = "careerName", target = "carrera")
    CareerDto carreerToCareerDto(Career career);

    @InheritInverseConfiguration
    Career careerDtoToCareer(CareerDto careerDto);


}
