package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

    Optional<CompanyProfile> findByIdCorporatePartnership(Long idCorporatePartnership);

    List<CompanyProfile> findByIdCorporatePartnershipIn(Collection<Long> idsCorporatePartnership);
}
