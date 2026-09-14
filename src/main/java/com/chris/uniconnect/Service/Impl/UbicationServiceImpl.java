package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.UbicationMappers;
import com.chris.uniconnect.Model.Dto.UbicationDto;
import com.chris.uniconnect.Model.Entity.Ubication;
import com.chris.uniconnect.Repository.UbicationRepository;
import com.chris.uniconnect.Service.IUbicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UbicationServiceImpl implements IUbicationService {

    @Autowired
    private UbicationRepository ubicationRepository;

    @Override
    public List<UbicationDto> getUbications() {
        return UbicationMappers.INSTANCE.listUbicationToListDto(ubicationRepository.findAll());
    }

    @Override
    public List<UbicationDto> createUbication(List<UbicationDto> ubicationDto) {
        List<Ubication> nuevasUbicaciones = ubicationDto.stream()
                .filter(dto -> !ubicationRepository.existsByStateIgnoreCase(dto.getEstado()))
                .map(UbicationMappers.INSTANCE::dtoToUbication)
                .toList();

        return UbicationMappers.INSTANCE.listUbicationToListDto(ubicationRepository.saveAll(nuevasUbicaciones));
    }

    @Override
    public boolean existUbication(Integer id) {
        return ubicationRepository.existsById(id);
    }

    @Override
    public void deleteUbication(Integer id) {
        ubicationRepository.deleteById(id);
    }
}
