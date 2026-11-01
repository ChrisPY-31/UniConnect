package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.JobApplicantDto;
import com.chris.uniconnect.Model.Dto.JobApplicationDto;
import com.chris.uniconnect.Model.Dto.JobApplicationJobDto;
import com.chris.uniconnect.Model.Entity.JobApplication;
import com.chris.uniconnect.Model.Entity.PostJob;
import com.chris.uniconnect.Model.Entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = CorporatePartnershipsMappers.class)
public interface JobApplicationMappers {

    JobApplicationMappers INSTANCE = Mappers.getMapper(JobApplicationMappers.class);

    @Mapping(source = "idJobApplication", target = "idPostulacion")
    @Mapping(source = "aplicationStatus", target = "estado")
    @Mapping(source = "coverLetter", target = "mensaje")
    @Mapping(source = "resumeUrl", target = "curriculum")
    @Mapping(source = "appliedAt", target = "fechaPostulacion")
    @Mapping(source = "updatedAt", target = "actualizada")
    @Mapping(source = "postJob", target = "vacante")
    @Mapping(source = "student", target = "estudiante")
    JobApplicationDto toDto(JobApplication jobApplication);

    List<JobApplicationDto> toDtoList(List<JobApplication> jobApplications);

    @Mapping(source = "idPostJob", target = "idVacante")
    @Mapping(source = "title", target = "titulo")
    @Mapping(source = "effectiveStatus", target = "estado")
    @Mapping(source = "corporatePartnershipsUAEMex", target = "empresa")
    JobApplicationJobDto toJobDto(PostJob postJob);

    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "lastName", target = "apellido")
    @Mapping(source = "image", target = "imagen")
    @Mapping(source = "career.careerName", target = "carrera")
    @Mapping(source = "semester", target = "semestre")
    JobApplicantDto toApplicantDto(Student student);
}
