package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PublicationDto;

import java.util.List;
import java.util.Optional;

public interface IPublicationService {

    List<PublicationDto> getAllPublications();

    PublicationDto createPublication(PublicationDto publicationDto);

    void deletePublication(PublicationDto publicationDto);

    Boolean existsPublication(Integer idPublication);

    PublicationDto publicationById(Integer idPublication);
}
