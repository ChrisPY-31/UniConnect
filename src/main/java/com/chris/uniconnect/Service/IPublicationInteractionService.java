package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PublicationInteractionDto;

import java.util.List;

public interface IPublicationInteractionService {
    List<PublicationInteractionDto> getPublications();

    PublicationInteractionDto createPublicationUsers(String username, PublicationInteractionDto publication);

}
