package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.PublicationInteraction;
import com.chris.uniconnect.Model.Entity.PublicationInteractionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationInteractionRepository extends JpaRepository<PublicationInteraction, PublicationInteractionPK> {

}
