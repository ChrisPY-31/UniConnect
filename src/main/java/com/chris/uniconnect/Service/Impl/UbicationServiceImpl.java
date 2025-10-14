package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.UbicationMappers;
import com.chris.uniconnect.Model.Dto.UbicationDto;
import com.chris.uniconnect.Repository.UbicationRepository;
import com.chris.uniconnect.Service.IUbicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UbicationServiceImpl implements IUbicationService {

    @Autowired
    private UbicationRepository ubicationRepository;

    @Override
    public UbicationDto createUbication(UbicationDto ubicationDto) {
        return UbicationMappers.INSTANCE.UbicationToUbicationDto(ubicationRepository.save(UbicationMappers.INSTANCE.UbicationDtoToUbication(ubicationDto)));
    }

    @Override
    public UbicationDto updateUbication(UbicationDto ubicationDto) {
        return UbicationMappers.INSTANCE.UbicationToUbicationDto(ubicationRepository.save(UbicationMappers.INSTANCE.UbicationDtoToUbication(ubicationDto)));

    }

    @Override
    public void deleteUbication(Integer idUbication) {
        ubicationRepository.deleteById(idUbication);
    }

    @Override
    public boolean existUbication(Integer id) {
        return ubicationRepository.existsById(id);
    }
}
