package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.PostJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostJobRepository extends JpaRepository<PostJob, Long>, JpaSpecificationExecutor<PostJob> {

    List<PostJob> findByIdRecruiterOrderByCreatedAtDesc(Integer idRecruiter);

    List<PostJob> findByIdCorporatePartnershipOrderByCreatedAtDesc(Long idCorporatePartnership);

    //una vacante de otro reclutador/empresa se trata como inexistente
    Optional<PostJob> findByIdPostJobAndIdRecruiter(Long idPostJob, Integer idRecruiter);

    Optional<PostJob> findByIdPostJobAndIdCorporatePartnership(Long idPostJob, Long idCorporatePartnership);
}
