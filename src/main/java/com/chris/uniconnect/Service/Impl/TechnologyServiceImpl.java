package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.TechnologyMappers;
import com.chris.uniconnect.Model.Dto.TechnologyDto;
import com.chris.uniconnect.Repository.TechnologyRepository;
import com.chris.uniconnect.Service.ITechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyServiceImpl implements ITechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;

    @Override
    public boolean existTechnology(Integer id) {
        return technologyRepository.existsById(id);
    }

    @Override
    public List<TechnologyDto> createTechnology(List<TechnologyDto> technologyDto) {
        return TechnologyMappers.INSTANCE.listTechnologyToListTechnonlogyDto(technologyRepository.saveAll(TechnologyMappers.INSTANCE.listTechnologyDtoToListTechnonlogy(technologyDto)));
    }

    @Override
    public void deleteTechnology(Integer id) {
        technologyRepository.deleteById(id);
    }
}
