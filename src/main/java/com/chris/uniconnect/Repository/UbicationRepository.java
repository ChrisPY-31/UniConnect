package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Ubication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicationRepository extends JpaRepository<Ubication, Integer> {

    boolean existsByStateIgnoreCase(String state);
}
