package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.SkillsRequest;

import java.util.Set;

public interface IPersonService {

    void deletePerson(PersonDto person);

    PersonDto getPersonsById(Integer id);

    PersonDto getPersonByUserName(String username);

    PersonDto updateAptitudes(String username, Set<Integer> aptitudeIds);

    PersonDto updateSkills(String username, SkillsRequest skills);
}
