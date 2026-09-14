package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Enum.ContactType;
import com.chris.uniconnect.Model.Entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    boolean existsByContact(ContactType contact);
}
