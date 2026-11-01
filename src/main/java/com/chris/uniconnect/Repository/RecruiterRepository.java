package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter , Integer> {

    Recruiter findByUserEntityUsername(String username);

    List<Recruiter> findByIdCorporatePartnership(Long idCorporatePartnership);

    List<Recruiter> findByIdCorporatePartnershipAndUserEntityIsEnabled(Long idCorporatePartnership, boolean isEnabled);

    //un reclutador de otro convenio se trata como inexistente
    Optional<Recruiter> findByIdAndIdCorporatePartnership(Integer id, Long idCorporatePartnership);
}
