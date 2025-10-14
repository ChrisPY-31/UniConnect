package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PersonDto;


public interface IPersonService {

    void deletePerson(PersonDto person);

    PersonDto getPersonsById(Integer id);

}
