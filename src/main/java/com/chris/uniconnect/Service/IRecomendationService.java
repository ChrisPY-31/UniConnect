package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.RecomendationDto;
import com.chris.uniconnect.Model.Entity.RecomendationPk;

import java.util.List;

public interface IRecomendationService {

    List<RecomendationDto> getRecomendations();

    RecomendationDto saveRecomendation(RecomendationDto recomendationDto);

    RecomendationDto getIdRecomendation(RecomendationPk id);

    boolean existsRecomendation(RecomendationPk id);

    void deleteRecomendation(RecomendationDto recomendationDto);

}
