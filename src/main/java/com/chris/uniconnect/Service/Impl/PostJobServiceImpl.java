package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.VacanteStatus;
import com.chris.uniconnect.Enum.WorkModality;
import com.chris.uniconnect.Enum.WorkingDay;
import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.PostJobMappers;
import com.chris.uniconnect.Model.Dto.PostJobDto;
import com.chris.uniconnect.Model.Dto.PostJobRequest;
import com.chris.uniconnect.Model.Entity.*;
import com.chris.uniconnect.Repository.*;
import com.chris.uniconnect.Service.IJobApplicationService;
import com.chris.uniconnect.Service.IPostJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.chris.uniconnect.Repository.PostJobSpecifications.*;

@Service
public class PostJobServiceImpl implements IPostJobService {

    private static final int MAX_TECHNOLOGIES = 15;

    @Autowired
    private PostJobRepository postJobRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private CompanyAdminRepository companyAdminRepository;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private IJobApplicationService jobApplicationService;

    @Override
    @Transactional
    public PostJobDto createJob(String recruiterUsername, PostJobRequest request) {
        Recruiter recruiter = findRecruiter(recruiterUsername);

        PostJob postJob = new PostJob();
        postJob.setIdRecruiter(recruiter.getId());
        postJob.setRecruiter(recruiter);
        postJob.setIdCorporatePartnership(recruiter.getIdCorporatePartnership());
        postJob.setCorporatePartnershipsUAEMex(recruiter.getCorporatePartnershipsUAEMex());
        postJob.setStatus(VacanteStatus.BORRADOR);
        postJob.setCreatedAt(LocalDateTime.now());
        applyRequest(postJob, request);

        return PostJobMappers.INSTANCE.toDto(postJobRepository.save(postJob));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostJobDto> getMyJobs(String recruiterUsername, VacanteStatus status) {
        List<PostJob> postJobs = postJobRepository.findByIdRecruiterOrderByCreatedAtDesc(findRecruiter(recruiterUsername).getId());
        return PostJobMappers.INSTANCE.toDtoList(filterByStatus(postJobs, status));
    }

    @Override
    @Transactional(readOnly = true)
    public PostJobDto getMyJob(String recruiterUsername, Long idPostJob) {
        return PostJobMappers.INSTANCE.toDto(findMyJob(recruiterUsername, idPostJob));
    }

    @Override
    @Transactional
    public PostJobDto updateMyJob(String recruiterUsername, Long idPostJob, PostJobRequest request) {
        PostJob postJob = findMyJob(recruiterUsername, idPostJob);
        if (postJob.getStatus() == VacanteStatus.CERRADA) {
            throw new BadRequestException("La vacante esta cerrada, ya no se puede editar");
        }
        applyRequest(postJob, request);
        //una vacante ya publicada (o pausada) no puede quedar incompleta
        if (postJob.getStatus() != VacanteStatus.BORRADOR) {
            validateComplete(postJob);
        }
        return PostJobMappers.INSTANCE.toDto(postJobRepository.save(postJob));
    }

    @Override
    @Transactional
    public PostJobDto changeMyJobStatus(String recruiterUsername, Long idPostJob, VacanteStatus status) {
        return changeStatus(findMyJob(recruiterUsername, idPostJob), status);
    }

    @Override
    @Transactional
    public void deleteMyJob(String recruiterUsername, Long idPostJob) {
        PostJob postJob = findMyJob(recruiterUsername, idPostJob);
        //las publicadas se cierran, no se borran: las postulaciones apuntan a ellas
        if (postJob.getStatus() != VacanteStatus.BORRADOR) {
            throw new BadRequestException("Solo se pueden eliminar vacantes en borrador; cierra la vacante en su lugar");
        }
        postJobRepository.delete(postJob);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostJobDto> getCompanyJobs(String companyAdminUsername, VacanteStatus status, Integer idRecruiter) {
        Long idPartnership = findCompanyAdmin(companyAdminUsername).getIdCorporatePartnership();
        List<PostJob> postJobs = postJobRepository.findByIdCorporatePartnershipOrderByCreatedAtDesc(idPartnership);
        if (idRecruiter != null) {
            postJobs = postJobs.stream().filter(postJob -> idRecruiter.equals(postJob.getIdRecruiter())).toList();
        }
        return PostJobMappers.INSTANCE.toDtoList(filterByStatus(postJobs, status));
    }

    @Override
    @Transactional
    public PostJobDto changeCompanyJobStatus(String companyAdminUsername, Long idPostJob, VacanteStatus status) {
        Long idPartnership = findCompanyAdmin(companyAdminUsername).getIdCorporatePartnership();
        PostJob postJob = postJobRepository.findByIdPostJobAndIdCorporatePartnership(idPostJob, idPartnership)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante", "id", idPostJob));
        //la empresa modera, pero no publica borradores de sus reclutadores
        if (postJob.getStatus() == VacanteStatus.BORRADOR) {
            throw new BadRequestException("La vacante es un borrador, solo su reclutador puede publicarla");
        }
        return changeStatus(postJob, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostJobDto> searchJobs(String text, TypeApplication type, WorkingDay workDay, WorkModality modality,
                                       Integer idCareer, Integer idTechnology, Long idPartnership, Pageable pageable) {
        Specification<PostJob> specification = Specification.allOf(
                visible(),
                PostJobSpecifications.text(text),
                typeApplication(type),
                workDay(workDay),
                modality(modality),
                career(idCareer),
                technology(idTechnology),
                partnership(idPartnership)
        );
        return postJobRepository.findAll(specification, pageable).map(PostJobMappers.INSTANCE::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PostJobDto getPublicJob(Long idPostJob) {
        PostJob postJob = postJobRepository.findOne(Specification.allOf(visible(), hasId(idPostJob)))
                .orElseThrow(() -> new ResourceNotFoundException("Vacante", "id", idPostJob));
        return PostJobMappers.INSTANCE.toDto(postJob);
    }

    //BORRADOR -> PUBLICADA <-> PAUSADA -> CERRADA; VENCIDA y BORRADOR no son destinos validos
    private PostJobDto changeStatus(PostJob postJob, VacanteStatus target) {
        VacanteStatus current = postJob.getStatus();
        if (target == null || target == VacanteStatus.BORRADOR || target == VacanteStatus.VENCIDA) {
            throw new BadRequestException("Estado destino no valido. Valores permitidos: PUBLICADA, PAUSADA, CERRADA");
        }
        if (current == VacanteStatus.CERRADA) {
            throw new BadRequestException("La vacante esta cerrada, ya no puede cambiar de estado");
        }
        if (current == target) {
            throw new BadRequestException("La vacante ya esta en estado " + target);
        }
        if (target == VacanteStatus.PAUSADA && current != VacanteStatus.PUBLICADA) {
            throw new BadRequestException("Solo se puede pausar una vacante publicada");
        }
        if (target == VacanteStatus.CERRADA && current == VacanteStatus.BORRADOR) {
            throw new BadRequestException("Un borrador no se cierra, eliminalo");
        }
        if (target == VacanteStatus.PUBLICADA) {
            validateComplete(postJob);
            validateCanPublish(postJob);
            if (postJob.getPublicationDate() == null) {
                postJob.setPublicationDate(LocalDate.now());
            }
        }

        postJob.setStatus(target);
        postJob.setUpdatedAt(LocalDateTime.now());
        PostJob saved = postJobRepository.save(postJob);
        //nadie se queda esperando respuesta de una vacante cerrada
        if (target == VacanteStatus.CERRADA) {
            jobApplicationService.rejectOpenApplications(saved.getIdPostJob());
        }
        return PostJobMappers.INSTANCE.toDto(saved);
    }

    private void applyRequest(PostJob postJob, PostJobRequest request) {
        if (request.salarioMin() != null && request.salarioMax() != null && request.salarioMin() > request.salarioMax()) {
            throw new BadRequestException("El salario minimo no puede ser mayor que el maximo");
        }

        postJob.setTitle(request.titulo().trim());
        postJob.setDescription(request.descripcion());
        postJob.setUbication(request.ubicacion());
        postJob.setWorkDay(request.jornada());
        postJob.setModality(request.modalidad());
        postJob.setTypeApplication(request.tipo());
        postJob.setSalaryMin(request.salarioMin());
        postJob.setSalaryMax(request.salarioMax());
        postJob.setClosingDate(request.fechaCierre());
        postJob.setCareers(findCareers(request.idsCarreras()));
        postJob.setTechnologies(findTechnologies(request.idsTecnologias()));
        postJob.setUpdatedAt(LocalDateTime.now());
    }

    private Set<Career> findCareers(Set<Integer> idsCareers) {
        if (idsCareers == null || idsCareers.isEmpty()) {
            return new HashSet<>();
        }
        List<Career> careers = careerRepository.findAllById(idsCareers);
        if (careers.size() != idsCareers.size()) {
            throw new BadRequestException("Alguna de las carreras seleccionadas no existe");
        }
        return new HashSet<>(careers);
    }

    private Set<Technology> findTechnologies(Set<Integer> idsTechnologies) {
        if (idsTechnologies == null || idsTechnologies.isEmpty()) {
            return new HashSet<>();
        }
        if (idsTechnologies.size() > MAX_TECHNOLOGIES) {
            throw new BadRequestException("Solo puedes seleccionar hasta " + MAX_TECHNOLOGIES + " tecnologias");
        }
        List<Technology> technologies = technologyRepository.findAllById(idsTechnologies);
        if (technologies.size() != idsTechnologies.size()) {
            throw new BadRequestException("Alguna de las tecnologias seleccionadas no existe");
        }
        return new HashSet<>(technologies);
    }

    private void validateComplete(PostJob postJob) {
        if (postJob.getDescription() == null || postJob.getDescription().isBlank()
                || postJob.getWorkDay() == null || postJob.getModality() == null || postJob.getTypeApplication() == null) {
            throw new BadRequestException("Para publicar, la vacante necesita descripcion, jornada, modalidad y tipo");
        }
    }

    //el convenio vencido por fecha deja entrar a la empresa, pero no publicar
    private void validateCanPublish(PostJob postJob) {
        CorporatePartnershipsUAEMex partnership = postJob.getCorporatePartnershipsUAEMex();
        if (partnership == null || !partnership.isVigente()) {
            throw new BadRequestException("El convenio de la empresa no esta vigente, no se pueden publicar vacantes");
        }
        if (postJob.getClosingDate() != null && postJob.getClosingDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("La fecha de cierre ya paso, actualizala antes de publicar");
        }
    }

    private List<PostJob> filterByStatus(List<PostJob> postJobs, VacanteStatus status) {
        if (status == null) {
            return postJobs;
        }
        return postJobs.stream().filter(postJob -> postJob.getEffectiveStatus() == status).toList();
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

    private PostJob findMyJob(String recruiterUsername, Long idPostJob) {
        Integer idRecruiter = findRecruiter(recruiterUsername).getId();
        return postJobRepository.findByIdPostJobAndIdRecruiter(idPostJob, idRecruiter)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante", "id", idPostJob));
    }
}
