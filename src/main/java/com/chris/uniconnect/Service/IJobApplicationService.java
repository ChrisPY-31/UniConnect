package com.chris.uniconnect.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.chris.uniconnect.Model.Dto.Response.ApplicationStatsResponse;
import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Model.Dto.JobApplicationDto;
import com.chris.uniconnect.Model.Dto.JobApplicationRequest;

import java.util.List;

public interface IJobApplicationService {

    //STUDENT
    JobApplicationDto apply(String studentUsername, Long idPostJob, JobApplicationRequest request);

    List<JobApplicationDto> getMyApplications(String studentUsername, AplicationsStatus status);

    JobApplicationDto withdraw(String studentUsername, Long idJobApplication);

    //RECRUITER: solo postulaciones a sus propias vacantes
    Page<JobApplicationDto> getApplicationsForMyJob(String recruiterUsername, Long idPostJob, AplicationsStatus status, String text, Pageable pageable);

    ApplicationStatsResponse getApplicationsForMyJobSummary(String recruiterUsername, Long idPostJob);

    JobApplicationDto changeStatus(String recruiterUsername, Long idJobApplication, AplicationsStatus status);

    //COMPANY_ADMIN: solo lectura
    Page<JobApplicationDto> getCompanyApplications(String companyAdminUsername, Long idPostJob, AplicationsStatus status, String text, Pageable pageable);

    ApplicationStatsResponse getCompanyApplicationsSummary(String companyAdminUsername, Long idPostJob);

    //al cerrar una vacante, sus postulaciones abiertas pasan a RECHAZADA
    void rejectOpenApplications(Long idPostJob);
}
