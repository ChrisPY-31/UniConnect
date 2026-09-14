package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.AptitudeMappers;
import com.chris.uniconnect.Model.Dto.AptitudeDto;
import com.chris.uniconnect.Model.Entity.Aptitude;
import com.chris.uniconnect.Repository.AptitudeRepository;
import com.chris.uniconnect.Service.IAptitudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AptitudeServiceImpl implements IAptitudeService {

    @Autowired
    private AptitudeRepository aptitudeRepository;

    @Override
    public List<AptitudeDto> getAptitudes() {
        return AptitudeMappers.INSTANCE.listAptitudeToListAptitudeDto(aptitudeRepository.findAll());
    }

    @Override
    public List<AptitudeDto> createAptitud(List<AptitudeDto> aptitudeDto) {
        List<Aptitude> nuevasAptitudes = aptitudeDto.stream()
                .filter(dto -> !aptitudeRepository.existsByNameIgnoreCase(dto.getNombre()))
                .map(AptitudeMappers.INSTANCE::aptitudeDtoToAptitude)
                .toList();

        return AptitudeMappers.INSTANCE.listAptitudeToListAptitudeDto(aptitudeRepository.saveAll(nuevasAptitudes));
    }

    @Override
    public boolean existAptitude(Integer id) {
        return aptitudeRepository.existsById(id);
    }

    @Override
    public void deleteAptitude(Integer id) {
        aptitudeRepository.deleteById(id);
    }
}
