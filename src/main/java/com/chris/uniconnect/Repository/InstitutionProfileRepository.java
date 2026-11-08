package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.InstitutionProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionProfileRepository extends JpaRepository<InstitutionProfile, Integer> {

    //es una sola fila; si por error hubiera mas, manda la primera
    Optional<InstitutionProfile> findFirstByOrderByIdInstitutionProfileAsc();
}
