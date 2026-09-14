package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.PublicationMappers;
import com.chris.uniconnect.Model.Dto.PublicationDto;
import com.chris.uniconnect.Model.Entity.Publication;
import com.chris.uniconnect.Repository.PublicationRepository;
import com.chris.uniconnect.Service.IPersonService;
import com.chris.uniconnect.Service.IPublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationServiceImpl implements IPublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private IPersonService personService;

    @Override
    public List<PublicationDto> getAllPublications() {
        return PublicationMappers.INSTANCE.publicationListToPublicacionDtoList(publicationRepository.findAll());
    }

    @Override
    public PublicationDto createPublication(String username, PublicationDto publicationDto) {
        Integer personId = personService.getPersonByUserName(username).getId();
        publicationDto.setIdPersona(personId);

        Publication savedPublication = publicationRepository.save(PublicationMappers.INSTANCE.publicacionDtoToPublication(publicationDto));
        return PublicationMappers.INSTANCE.publicationDtoToPublicacionDto(savedPublication);
    }

    @Override
    public PublicationDto updatePublication(String username, Integer idPublication, PublicationDto publicationDto) {
        Integer personId = personService.getPersonByUserName(username).getId();

        Publication existingPublication = publicationRepository.findById(idPublication)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", idPublication));

        if (!personId.equals(existingPublication.getIdPerson())) {
            throw new BadRequestException("No tienes permiso para modificar esta publicacion");
        }

        publicationDto.setIdPersona(personId);
        PublicationMappers.INSTANCE.updatePublicationFromDto(publicationDto, existingPublication);

        return PublicationMappers.INSTANCE.publicationDtoToPublicacionDto(publicationRepository.save(existingPublication));
    }

    @Override
    public void deletePublication(String username, Integer idPublication) {
        Integer personId = personService.getPersonByUserName(username).getId();

        Publication existingPublication = publicationRepository.findById(idPublication)
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion", "id", idPublication));

        if (!personId.equals(existingPublication.getIdPerson())) {
            throw new BadRequestException("No tienes permiso para eliminar esta publicacion");
        }

        publicationRepository.delete(existingPublication);
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
