package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.RecomendationMappers;
import com.chris.uniconnect.Model.Dto.RecomendationDto;
import com.chris.uniconnect.Repository.RecomendationRepository;
import com.chris.uniconnect.Service.IRecomendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecomendatoinServiceImpl implements IRecomendationService {

    @Autowired
    private RecomendationRepository recomendationRepository;

    @Override
    public List<RecomendationDto> getRecomendations() {
        return RecomendationMappers.INSTANCE.listRecomendationToListDto(recomendationRepository.findAll());
    }

    @Override
    public RecomendationDto saveRecomendation(RecomendationDto recomendationDto) {
        return RecomendationMappers.INSTANCE.recomendationToRecomendationDto( recomendationRepository.save(RecomendationMappers.INSTANCE.recomendationDtoToRecomendation(recomendationDto)));
    }

    @Override
    public RecomendationDto getIdRecomendation(int id) {
        return RecomendationMappers.INSTANCE.recomendationToRecomendationDto( recomendationRepository.findById(id).orElse(null));
    }

    @Override
    public boolean existsRecomendation(int id) {
        return recomendationRepository.existsById(id);
    }

    @Override
    public void deleteRecomendation(RecomendationDto recomendationDto) {
        recomendationRepository.delete(RecomendationMappers.INSTANCE.recomendationDtoToRecomendation(recomendationDto));
    }
}
