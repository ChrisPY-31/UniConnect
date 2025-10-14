package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.PersonContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonContactRepository extends JpaRepository<PersonContact , Integer> {
}
