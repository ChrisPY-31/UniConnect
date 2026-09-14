package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PersonDto;

import java.util.Set;

public interface IPersonService {

    void deletePerson(PersonDto person);

    PersonDto getPersonsById(Integer id);

    PersonDto getPersonByUserName(String username);

    PersonDto updateAptitudes(String username, Set<Integer> aptitudeIds);
}
