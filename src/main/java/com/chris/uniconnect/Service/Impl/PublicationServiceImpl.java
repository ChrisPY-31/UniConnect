package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.PublicationMappers;
import com.chris.uniconnect.Model.Dto.PublicationDto;
import com.chris.uniconnect.Repository.PublicationRepository;
import com.chris.uniconnect.Service.IPublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationServiceImpl implements IPublicationService {

    @Autowired
    private PublicationRepository publicationRepository;


    @Override
    public List<PublicationDto> getAllPublications() {
        return PublicationMappers.INSTANCE.publicationListToPublicacionDtoList(publicationRepository.findAll());
    }

    @Override
    public PublicationDto createPublication(PublicationDto publicationDto) {
        return PublicationMappers.INSTANCE.publicationDtoToPublicacionDto(publicationRepository.save(PublicationMappers.INSTANCE.publicacionDtoToPublication(publicationDto)));
    }

    @Override
    public void deletePublication(PublicationDto publicationDto) {
        publicationRepository.deleteById(publicationDto.getId());
    }

    @Override
    public Boolean existsPublication(Integer idPublication) {
        return publicationRepository.existsById(idPublication);
    }

    @Override
    public PublicationDto publicationById(Integer idPublication) {
        return PublicationMappers.INSTANCE.publicationDtoToPublicacionDto(publicationRepository.findById(idPublication).orElse(null));
    }


}
