package com.chris.uniconnect.Service;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.VacanteStatus;
import com.chris.uniconnect.Enum.WorkModality;
import com.chris.uniconnect.Enum.WorkingDay;
import com.chris.uniconnect.Model.Dto.PostJobDto;
import com.chris.uniconnect.Model.Dto.PostJobRequest;
import com.chris.uniconnect.Model.Dto.Response.JobOptionResponse;
import com.chris.uniconnect.Model.Dto.Response.JobStatsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostJobService {

    //RECRUITER: solo sus propias vacantes
    PostJobDto createJob(String recruiterUsername, PostJobRequest request);

    Page<PostJobDto> getMyJobs(String recruiterUsername, VacanteStatus status, String text, TypeApplication type, Pageable pageable);

    JobStatsResponse getMyJobsSummary(String recruiterUsername);

    List<JobOptionResponse> getMyJobOptions(String recruiterUsername);

    PostJobDto getMyJob(String recruiterUsername, Long idPostJob);

    PostJobDto updateMyJob(String recruiterUsername, Long idPostJob, PostJobRequest request);

    PostJobDto changeMyJobStatus(String recruiterUsername, Long idPostJob, VacanteStatus status);

    void deleteMyJob(String recruiterUsername, Long idPostJob);

    //COMPANY_ADMIN: ve todas las de su empresa y solo modera el estado
    Page<PostJobDto> getCompanyJobs(String companyAdminUsername, VacanteStatus status, Integer idRecruiter, String text, TypeApplication type, Pageable pageable);

    JobStatsResponse getCompanyJobsSummary(String companyAdminUsername, Integer idRecruiter);

    List<JobOptionResponse> getCompanyJobOptions(String companyAdminUsername);

    PostJobDto changeCompanyJobStatus(String companyAdminUsername, Long idPostJob, VacanteStatus status);

    //publico: solo vacantes visibles
    Page<PostJobDto> searchJobs(String text, TypeApplication type, WorkingDay workDay, WorkModality modality,
                                Integer idCareer, Integer idTechnology, Long idPartnership, Pageable pageable);

    PostJobDto getPublicJob(Long idPostJob);
}
