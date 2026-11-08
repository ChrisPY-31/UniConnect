package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorporatePartnershipsRepository extends ListCrudRepository<CorporatePartnershipsUAEMex, Long> {

    boolean existsByCompanyNameIgnoreCase(String companyName);

    Optional<CorporatePartnershipsUAEMex> findByCompanyNameIgnoreCase(String companyName);

    boolean existsByCompanyNameIgnoreCaseAndIdCorporatePartnershipNot(String companyName, Long idCorporatePartnership);
}
