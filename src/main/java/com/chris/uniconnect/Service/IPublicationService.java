package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PublicationDto;

import java.util.List;

public interface IPublicationService {

    List<PublicationDto> getAllPublications();

    PublicationDto createPublication(String username, PublicationDto publicationDto);

    PublicationDto updatePublication(String username, Integer idPublication, PublicationDto publicationDto);

    void deletePublication(String username, Integer idPublication);

    Boolean existsPublication(Integer idPublication);

    PublicationDto publicationById(Integer idPublication);
}
