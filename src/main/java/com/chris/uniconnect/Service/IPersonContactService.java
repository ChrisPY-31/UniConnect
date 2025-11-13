package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PersonContactDto;
import com.chris.uniconnect.Model.Entity.PersonContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPersonContactService {

    List<PersonContactDto> createPersonContact(List<PersonContactDto> personContact);
}
