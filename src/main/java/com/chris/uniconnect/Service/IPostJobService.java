package com.chris.uniconnect.Service;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.VacanteStatus;
import com.chris.uniconnect.Enum.WorkModality;
import com.chris.uniconnect.Enum.WorkingDay;
import com.chris.uniconnect.Model.Dto.PostJobDto;
import com.chris.uniconnect.Model.Dto.PostJobRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostJobService {

    //RECRUITER: solo sus propias vacantes
    PostJobDto createJob(String recruiterUsername, PostJobRequest request);

    List<PostJobDto> getMyJobs(String recruiterUsername, VacanteStatus status);

    PostJobDto getMyJob(String recruiterUsername, Long idPostJob);

    PostJobDto updateMyJob(String recruiterUsername, Long idPostJob, PostJobRequest request);

    PostJobDto changeMyJobStatus(String recruiterUsername, Long idPostJob, VacanteStatus status);

    void deleteMyJob(String recruiterUsername, Long idPostJob);

    //COMPANY_ADMIN: ve todas las de su empresa y solo modera el estado
    List<PostJobDto> getCompanyJobs(String companyAdminUsername, VacanteStatus status, Integer idRecruiter);

    PostJobDto changeCompanyJobStatus(String companyAdminUsername, Long idPostJob, VacanteStatus status);

    //publico: solo vacantes visibles
    Page<PostJobDto> searchJobs(String text, TypeApplication type, WorkingDay workDay, WorkModality modality,
                                Integer idCareer, Integer idTechnology, Long idPartnership, Pageable pageable);

    PostJobDto getPublicJob(Long idPostJob);
}
