package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.PublicationInteractionMappers;
import com.chris.uniconnect.Model.Dto.PublicationInteractionDto;
import com.chris.uniconnect.Repository.PublicationInteractionRepository;
import com.chris.uniconnect.Service.IPublicationInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationInteractionServiceImpl implements IPublicationInteractionService {

    @Autowired
    private PublicationInteractionRepository publicationInteractionRepository;

    @Override
    public List<PublicationInteractionDto> getPublications() {
        return PublicationInteractionMappers.INSTANCE.entityToPublicationInteractionDtoList(publicationInteractionRepository.findAll());
    }

    @Override
    public PublicationInteractionDto createPublicationUsers(PublicationInteractionDto publicationInteractionDto) {
        System.out.println("entramos al impl");
        System.out.println(publicationInteractionDto.getComentario());
        System.out.println(publicationInteractionDto.getComentario());
        return PublicationInteractionMappers.INSTANCE.entityToPublicationInteractionDto(
                publicationInteractionRepository.save(PublicationInteractionMappers.INSTANCE.DtoToPublicationInteraction(publicationInteractionDto)));
    }

    @Override
    public Boolean existPublicatio(Integer id) {
        return publicationInteractionRepository.existsById(id);
    }
}
