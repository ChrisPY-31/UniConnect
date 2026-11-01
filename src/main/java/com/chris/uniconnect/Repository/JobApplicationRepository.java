package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Model.Entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByIdStudentAndIdPostJob(Integer idStudent, Long idPostJob);

    List<JobApplication> findByIdStudentOrderByAppliedAtDesc(Integer idStudent);

    List<JobApplication> findByIdPostJobOrderByAppliedAtDesc(Long idPostJob);

    List<JobApplication> findByPostJobIdCorporatePartnershipOrderByAppliedAtDesc(Long idCorporatePartnership);

    List<JobApplication> findByIdPostJobAndAplicationStatusIn(Long idPostJob, Collection<AplicationsStatus> statuses);

    //una postulacion ajena se trata como inexistente
    Optional<JobApplication> findByIdJobApplicationAndIdStudent(Long idJobApplication, Integer idStudent);

    Optional<JobApplication> findByIdJobApplicationAndPostJobIdRecruiter(Long idJobApplication, Integer idRecruiter);
}
