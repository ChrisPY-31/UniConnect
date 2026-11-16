package com.chris.uniconnect.Service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.chris.uniconnect.Model.Dto.Response.ApplicationStatsResponse;
import com.chris.uniconnect.Repository.JobApplicationSpecifications;
import java.util.LinkedHashMap;
import java.util.Map;
import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.JobApplicationMappers;
import com.chris.uniconnect.Model.Dto.JobApplicationDto;
import com.chris.uniconnect.Model.Dto.JobApplicationRequest;
import com.chris.uniconnect.Model.Entity.*;
import com.chris.uniconnect.Repository.*;
import com.chris.uniconnect.Service.IJobApplicationService;
import com.chris.uniconnect.Service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.chris.uniconnect.Repository.PostJobSpecifications.hasId;
import static com.chris.uniconnect.Repository.PostJobSpecifications.visible;

@Service
public class JobApplicationServiceImpl implements IJobApplicationService {

    private static final List<AplicationsStatus> OPEN_STATUSES =
            List.of(AplicationsStatus.ENVIADA, AplicationsStatus.EN_REVISION, AplicationsStatus.ENTREVISTA);

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private PostJobRepository postJobRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private CompanyAdminRepository companyAdminRepository;

    @Autowired
    private INotificationService notificationService;

    @Override
    @Transactional
    public JobApplicationDto apply(String studentUsername, Long idPostJob, JobApplicationRequest request) {
        Student student = findStudent(studentUsername);

        //solo vacantes que el estudiante puede ver: publicada, no vencida y de convenio vigente
        PostJob postJob = postJobRepository.findOne(Specification.allOf(visible(), hasId(idPostJob)))
                .orElseThrow(() -> new ResourceNotFoundException("Vacante", "id", idPostJob));

        if (student.getResumeUrl() == null || student.getResumeUrl().isBlank()) {
            throw new BadRequestException("Necesitas subir tu CV (PATCH /api/v1/student/me/resume) antes de postularte");
        }
        if (jobApplicationRepository.existsByIdStudentAndIdPostJob(student.getId(), idPostJob)) {
            throw new BadRequestException("Ya te postulaste a esta vacante");
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setIdStudent(student.getId());
        jobApplication.setStudent(student);
        jobApplication.setIdPostJob(idPostJob);
        jobApplication.setPostJob(postJob);
        jobApplication.setResumeUrl(student.getResumeUrl());
        jobApplication.setCoverLetter(request == null ? null : request.mensaje());
        jobApplication.setAplicationStatus(AplicationsStatus.ENVIADA);
        jobApplication.setAppliedAt(LocalDateTime.now());
        JobApplication saved = jobApplicationRepository.save(jobApplication);

        notificationService.notifyJobApplicationReceived(postJob.getIdRecruiter(), idPostJob,
                student.getName() + " " + student.getLastName(), postJob.getTitle());

        return JobApplicationMappers.INSTANCE.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationDto> getMyApplications(String studentUsername, AplicationsStatus status) {
        List<JobApplication> applications = jobApplicationRepository.findByIdStudentOrderByAppliedAtDesc(findStudent(studentUsername).getId());
        return JobApplicationMappers.INSTANCE.toDtoList(filterByStatus(applications, status));
    }

    @Override
    @Transactional
    public JobApplicationDto withdraw(String studentUsername, Long idJobApplication) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdJobApplicationAndIdStudent(idJobApplication, findStudent(studentUsername).getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulacion", "id", idJobApplication));
        if (jobApplication.getAplicationStatus().isFinal()) {
            throw new BadRequestException("La postulacion ya esta en estado " + jobApplication.getAplicationStatus() + ", no se puede retirar");
        }
        jobApplication.setAplicationStatus(AplicationsStatus.RETIRADA);
        jobApplication.setUpdatedAt(LocalDateTime.now());
        return JobApplicationMappers.INSTANCE.toDto(jobApplicationRepository.save(jobApplication));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationDto> getApplicationsForMyJob(String recruiterUsername, Long idPostJob, AplicationsStatus status, String text, Pageable pageable) {
        checkMyJob(recruiterUsername, idPostJob);
        Specification<JobApplication> specification = Specification.allOf(
                JobApplicationSpecifications.postJob(idPostJob),
                JobApplicationSpecifications.status(status),
                JobApplicationSpecifications.text(text)
        );
        return jobApplicationRepository.findAll(specification, pageable).map(JobApplicationMappers.INSTANCE::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationStatsResponse getApplicationsForMyJobSummary(String recruiterUsername, Long idPostJob) {
        checkMyJob(recruiterUsername, idPostJob);
        return applicationStats(JobApplicationSpecifications.postJob(idPostJob));
    }

    //una vacante de otro reclutador se trata como inexistente
    private void checkMyJob(String recruiterUsername, Long idPostJob) {
        Integer idRecruiter = findRecruiter(recruiterUsername).getId();
        if (postJobRepository.findByIdPostJobAndIdRecruiter(idPostJob, idRecruiter).isEmpty()) {
            throw new ResourceNotFoundException("Vacante", "id", idPostJob);
        }
    }

    private ApplicationStatsResponse applicationStats(Specification<JobApplication> base) {
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (AplicationsStatus status : AplicationsStatus.values()) {
            byStatus.put(status.name(), jobApplicationRepository.count(Specification.allOf(base, JobApplicationSpecifications.status(status))));
        }
        return new ApplicationStatsResponse(jobApplicationRepository.count(base), byStatus);
    }

    //solo hacia adelante: ENVIADA -> EN_REVISION -> ENTREVISTA -> ACEPTADA, o RECHAZADA desde cualquier estado abierto
    @Override
    @Transactional
    public JobApplicationDto changeStatus(String recruiterUsername, Long idJobApplication, AplicationsStatus status) {
        Integer idRecruiter = findRecruiter(recruiterUsername).getId();
        JobApplication jobApplication = jobApplicationRepository
                .findByIdJobApplicationAndPostJobIdRecruiter(idJobApplication, idRecruiter)
                .orElseThrow(() -> new ResourceNotFoundException("Postulacion", "id", idJobApplication));

        AplicationsStatus current = jobApplication.getAplicationStatus();
        if (status == null || status == AplicationsStatus.ENVIADA || status == AplicationsStatus.RETIRADA) {
            throw new BadRequestException("Estado destino no valido. Valores permitidos: EN_REVISION, ENTREVISTA, ACEPTADA, RECHAZADA");
        }
        if (current.isFinal()) {
            throw new BadRequestException("La postulacion ya esta en estado " + current + ", no puede cambiar");
        }
        if (status != AplicationsStatus.RECHAZADA && status.ordinal() <= current.ordinal()) {
            throw new BadRequestException("La postulacion ya esta en " + current + ", solo puede avanzar o rechazarse");
        }

        jobApplication.setAplicationStatus(status);
        jobApplication.setUpdatedAt(LocalDateTime.now());
        JobApplication saved = jobApplicationRepository.save(jobApplication);

        notificationService.notifyJobApplicationStatus(saved.getIdStudent(), idJobApplication, saved.getPostJob().getTitle(), status);

        return JobApplicationMappers.INSTANCE.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobApplicationDto> getCompanyApplications(String companyAdminUsername, Long idPostJob, AplicationsStatus status, String text, Pageable pageable) {
        Specification<JobApplication> specification = Specification.allOf(
                JobApplicationSpecifications.partnership(findCompanyAdmin(companyAdminUsername).getIdCorporatePartnership()),
                JobApplicationSpecifications.postJob(idPostJob),
                JobApplicationSpecifications.status(status),
                JobApplicationSpecifications.text(text)
        );
        return jobApplicationRepository.findAll(specification, pageable).map(JobApplicationMappers.INSTANCE::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationStatsResponse getCompanyApplicationsSummary(String companyAdminUsername, Long idPostJob) {
        return applicationStats(Specification.allOf(
                JobApplicationSpecifications.partnership(findCompanyAdmin(companyAdminUsername).getIdCorporatePartnership()),
                JobApplicationSpecifications.postJob(idPostJob)
        ));
    }

    @Override
    @Transactional
    public void rejectOpenApplications(Long idPostJob) {
        List<JobApplication> openApplications = jobApplicationRepository.findByIdPostJobAndAplicationStatusIn(idPostJob, OPEN_STATUSES);
        LocalDateTime now = LocalDateTime.now();
        for (JobApplication jobApplication : openApplications) {
            jobApplication.setAplicationStatus(AplicationsStatus.RECHAZADA);
            jobApplication.setUpdatedAt(now);
        }
        jobApplicationRepository.saveAll(openApplications);
        openApplications.forEach(jobApplication -> notificationService.notifyJobApplicationStatus(jobApplication.getIdStudent(),
                jobApplication.getIdJobApplication(), jobApplication.getPostJob().getTitle(), AplicationsStatus.RECHAZADA));
    }

    private List<JobApplication> filterByStatus(List<JobApplication> applications, AplicationsStatus status) {
        if (status == null) {
            return applications;
        }
        return applications.stream().filter(application -> application.getAplicationStatus() == status).toList();
    }

    private Student findStudent(String username) {
        Student student = studentRepository.findByUserEntityUsername(username);
        if (student == null) {
            throw new ResourceNotFoundException("Estudiante", "username", username);
        }
        return student;
    }

    private Recruiter findRecruiter(String username) {
        Recruiter recruiter = recruiterRepository.findByUserEntityUsername(username);
        if (recruiter == null) {
            throw new ResourceNotFoundException("Reclutador", "username", username);
        }
        return recruiter;
    }

    private CompanyAdmin findCompanyAdmin(String username) {
        CompanyAdmin companyAdmin = companyAdminRepository.findByUserEntityUsername(username);
        if (companyAdmin == null) {
            throw new ResourceNotFoundException("Cuenta de empresa", "username", username);
        }
        return companyAdmin;
    }
}
