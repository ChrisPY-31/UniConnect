package com.chris.uniconnect.Service.Impl;


import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.PersonContactMappers;
import com.chris.uniconnect.Model.Dto.PersonContactDto;
import com.chris.uniconnect.Model.Entity.PersonContact;
import com.chris.uniconnect.Model.Entity.PersonContactPK;
import com.chris.uniconnect.Repository.ContactRepository;
import com.chris.uniconnect.Repository.PersonContactRepository;
import com.chris.uniconnect.Service.IPersonContactService;
import com.chris.uniconnect.Service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonContactServiceImpl implements IPersonContactService {

    @Autowired
    private PersonContactRepository personContactRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private IPersonService personService;

    @Override
    public List<PersonContactDto> getMyContacts(String username) {
        Integer personId = personService.getPersonByUserName(username).getId();
        return PersonContactMappers.INSTANCE.toPersonContacDtoList(personContactRepository.findById_IdPerson(personId));
    }

    @Override
    public List<PersonContactDto> createPersonContact(String username, List<PersonContactDto> personContact) {
        Integer personId = personService.getPersonByUserName(username).getId();

        for (PersonContactDto dto : personContact) {
            if (dto.getId() == null || dto.getId().getIdContact() == null) {
                throw new BadRequestException("Se requiere el id del contacto (red) para cada registro");
            }
            if (!contactRepository.existsById(dto.getId().getIdContact())) {
                throw new ResourceNotFoundException("Contacto", "id", dto.getId().getIdContact());
            }
            dto.getId().setIdPerson(personId);
        }

        List<PersonContact> saved = personContactRepository.saveAll(PersonContactMappers.INSTANCE.toPersonContacList(personContact));
        return PersonContactMappers.INSTANCE.toPersonContacDtoList(saved);
    }

    @Override
    public void deletePersonContact(String username, Integer idContact) {
        Integer personId = personService.getPersonByUserName(username).getId();
        PersonContactPK id = new PersonContactPK(personId, idContact);

        if (!personContactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contacto de persona", "idContact", idContact);
        }
        personContactRepository.deleteById(id);
    }
}
