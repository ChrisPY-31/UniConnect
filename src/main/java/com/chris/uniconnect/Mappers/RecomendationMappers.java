package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.RecomendationDto;
import com.chris.uniconnect.Model.Entity.Recomendation;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = TeacherDetailMappers.class)
public interface RecomendationMappers {

    RecomendationMappers INSTANCE = Mappers.getMapper(RecomendationMappers.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "comment", target = "comentario")
    @Mapping(source = "recomendationDate", target = "fechaRecomendacion")
    @Mapping(source = "teacher", target = "maestro")
    RecomendationDto recomendationToRecomendationDto(Recomendation recomendation);

    @InheritInverseConfiguration
    Recomendation recomendationDtoToRecomendation(RecomendationDto recomendationDto);

    List<RecomendationDto> listRecomendationToListDto(List<Recomendation> recomendations);

}
