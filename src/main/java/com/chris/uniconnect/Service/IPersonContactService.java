package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PersonContactDto;

import java.util.List;

public interface IPersonContactService {

    List<PersonContactDto> getMyContacts(String username);

    List<PersonContactDto> createPersonContact(String username, List<PersonContactDto> personContact);

    void deletePersonContact(String username, Integer idContact);
}
