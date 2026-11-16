package com.chris.uniconnect.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorporatePartnershipsRepository extends JpaRepository<CorporatePartnershipsUAEMex, Long>, JpaSpecificationExecutor<CorporatePartnershipsUAEMex> {

    //sugerencias del formulario de convenio
    @Query("select distinct p.category from CorporatePartnershipsUAEMex p where p.category is not null and trim(p.category) <> '' order by p.category")
    List<String> findDistinctCategories();

    @Query("select distinct p.agreementType from CorporatePartnershipsUAEMex p where p.agreementType is not null and trim(p.agreementType) <> '' order by p.agreementType")
    List<String> findDistinctAgreementTypes();

    boolean existsByCompanyNameIgnoreCase(String companyName);

    Optional<CorporatePartnershipsUAEMex> findByCompanyNameIgnoreCase(String companyName);

    boolean existsByCompanyNameIgnoreCaseAndIdCorporatePartnershipNot(String companyName, Long idCorporatePartnership);
}
