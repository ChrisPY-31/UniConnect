package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PublicationDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IPublicationService {

    List<PublicationDto> getAllPublications();

    PublicationDto createPublication(PublicationDto publicationDto, MultipartFile file);

    void deletePublication(PublicationDto publicationDto);

    Boolean existsPublication(Integer idPublication);

    PublicationDto publicationById(Integer idPublication);
}
