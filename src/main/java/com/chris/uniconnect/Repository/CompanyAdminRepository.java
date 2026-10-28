package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.CompanyAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyAdminRepository extends JpaRepository<CompanyAdmin, Integer> {

    CompanyAdmin findByUserEntityUsername(String username);

    List<CompanyAdmin> findByIdCorporatePartnership(Long idCorporatePartnership);

    //regla: solo una cuenta de empresa activa por convenio
    @Query("select count(c) > 0 from CompanyAdmin c where c.idCorporatePartnership = :idPartnership " +
            "and c.userEntity.isEnabled = true and c.id <> :excludeId")
    boolean existsOtherActiveAccount(@Param("idPartnership") Long idPartnership, @Param("excludeId") Integer excludeId);
}
