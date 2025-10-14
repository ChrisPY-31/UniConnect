package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PublicationInteractionDto;
import com.chris.uniconnect.Model.Entity.Publication;

import java.util.List;

public interface IPublicationInteractionService {
    List<PublicationInteractionDto> getPublications();

    PublicationInteractionDto createPublicationUsers(PublicationInteractionDto publication);

    Boolean existPublicatio(Integer id);



}
