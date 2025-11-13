package com.chris.uniconnect.Service.Impl;


import com.chris.uniconnect.Mappers.PersonContactMappers;
import com.chris.uniconnect.Mappers.PersonMappers;
import com.chris.uniconnect.Model.Dto.PersonContactDto;
import com.chris.uniconnect.Repository.PersonContactRepository;
import com.chris.uniconnect.Service.IPersonContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonContactServiceImpl implements IPersonContactService {
    @Autowired
    private PersonContactRepository personContactRepository;

    @Override
    public List<PersonContactDto> createPersonContact(List<PersonContactDto> personContact) {

        return PersonContactMappers.INSTANCE.toPersonContacDtoList(personContactRepository.saveAll(PersonContactMappers.INSTANCE.toPersonContacList(personContact)));

        /*
        return PersonContactMappers.INSTANCE.personContactToPersonContactDto(personContactRepository.saveAll(PersonContactMappers.INSTANCE.toPersonContacList(personContact)));
        *
        * */
    }


}
