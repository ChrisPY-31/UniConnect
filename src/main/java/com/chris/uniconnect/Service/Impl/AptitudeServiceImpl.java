package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.AptitudeMappers;
import com.chris.uniconnect.Model.Dto.AptitudeDto;
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
    public List<AptitudeDto> createAptitud(List<AptitudeDto> aptitudeDto) {
        return AptitudeMappers.INSTANCE.toEntity(aptitudeRepository.saveAll(AptitudeMappers.INSTANCE.aptitudeToEntity(aptitudeDto)));
    }

    @Override
    public void deleteAptitude(AptitudeDto aptitudeDto) {

    }
}
