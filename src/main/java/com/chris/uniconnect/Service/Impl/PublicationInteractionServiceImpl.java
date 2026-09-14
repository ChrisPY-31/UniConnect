package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.PublicationInteractionMappers;
import com.chris.uniconnect.Model.Dto.PublicationInteractionDto;
import com.chris.uniconnect.Repository.PublicationInteractionRepository;
import com.chris.uniconnect.Repository.PublicationRepository;
import com.chris.uniconnect.Service.IPersonService;
import com.chris.uniconnect.Service.IPublicationInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationInteractionServiceImpl implements IPublicationInteractionService {

    @Autowired
    private PublicationInteractionRepository publicationInteractionRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private IPersonService personService;

    @Override
    public List<PublicationInteractionDto> getPublications() {
        return PublicationInteractionMappers.INSTANCE.entityToPublicationInteractionDtoList(publicationInteractionRepository.findAll());
    }

    @Override
    public PublicationInteractionDto createPublicationUsers(String username, PublicationInteractionDto publicationInteractionDto) {
        if (publicationInteractionDto.getId() == null || publicationInteractionDto.getId().getIdPublication() == null) {
            throw new BadRequestException("Se requiere el id de la publicacion para la interaccion");
        }
        if (!publicationRepository.existsById(publicationInteractionDto.getId().getIdPublication())) {
            throw new ResourceNotFoundException("Publicacion", "id", publicationInteractionDto.getId().getIdPublication());
        }

        Integer personId = personService.getPersonByUserName(username).getId();
        publicationInteractionDto.getId().setIdPerson(personId);

        return PublicationInteractionMappers.INSTANCE.entityToPublicationInteractionDto(
                publicationInteractionRepository.save(PublicationInteractionMappers.INSTANCE.DtoToPublicationInteraction(publicationInteractionDto)));
    }
}
