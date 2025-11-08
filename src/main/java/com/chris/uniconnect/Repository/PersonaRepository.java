package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Person , Integer> {

    Person findByUserEntityUsername(String username);

}
