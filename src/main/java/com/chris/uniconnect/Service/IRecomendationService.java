package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.RecomendationDto;

import java.util.List;

public interface IRecomendationService {

    List<RecomendationDto> getRecomendations();

    RecomendationDto saveRecomendation(RecomendationDto recomendationDto);

    RecomendationDto getIdRecomendation(int id);

    boolean existsRecomendation(int id);

    void deleteRecomendation(RecomendationDto recomendationDto);

}
