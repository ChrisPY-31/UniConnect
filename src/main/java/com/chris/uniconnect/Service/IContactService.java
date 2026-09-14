package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.ContactDto;

import java.util.List;

public interface IContactService {

    List<ContactDto> getContacts();
}
