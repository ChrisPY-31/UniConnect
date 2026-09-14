package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.PersonContactMappers;
import com.chris.uniconnect.Model.Dto.ContactDto;
import com.chris.uniconnect.Repository.ContactRepository;
import com.chris.uniconnect.Service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements IContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<ContactDto> getContacts() {
        return contactRepository.findAll().stream()
                .map(PersonContactMappers.INSTANCE::contactToContactDto)
                .collect(Collectors.toList());
    }
}
