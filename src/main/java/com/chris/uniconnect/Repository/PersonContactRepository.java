package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.PersonContact;
import com.chris.uniconnect.Model.Entity.PersonContactPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonContactRepository extends JpaRepository<PersonContact , PersonContactPK> {

    List<PersonContact> findById_IdPerson(Integer idPerson);
}
