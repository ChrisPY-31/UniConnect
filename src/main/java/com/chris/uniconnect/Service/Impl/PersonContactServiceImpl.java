package com.chris.uniconnect.Service.Impl;


import com.chris.uniconnect.Mappers.PersonContactMappers;
import com.chris.uniconnect.Model.Dto.PersonContactDto;
import com.chris.uniconnect.Repository.PersonContactRepository;
import com.chris.uniconnect.Service.IPersonContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonContactServiceImpl implements IPersonContactService {
    @Autowired
    private PersonContactRepository personContactRepository;

    @Override
    public PersonContactDto createPersonContact(PersonContactDto personContact) {
        return PersonContactMappers.INSTANCE.personContactToPersonContactDto(personContactRepository.save(PersonContactMappers.INSTANCE.PersonContactDtoToPersonContact(personContact)));
    }


}
