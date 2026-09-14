package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.RecomendationDto;

import java.util.List;

public interface IRecomendationService {

    List<RecomendationDto> getRecomendations();

    RecomendationDto createRecomendation(String username, RecomendationDto recomendationDto);

    RecomendationDto updateRecomendation(String username, Integer idStudent, RecomendationDto recomendationDto);

    void deleteRecomendation(String username, Integer idStudent);

}
