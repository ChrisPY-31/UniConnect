package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.JobRecruiterDto;
import com.chris.uniconnect.Model.Dto.PostJobDto;
import com.chris.uniconnect.Model.Entity.PostJob;
import com.chris.uniconnect.Model.Entity.Recruiter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        CareerMappers.class,
        TechnologyMappers.class,
        CorporatePartnershipsMappers.class
})
public interface PostJobMappers {

    PostJobMappers INSTANCE = Mappers.getMapper(PostJobMappers.class);

    @Mapping(source = "idPostJob", target = "idVacante")
    @Mapping(source = "title", target = "titulo")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "ubication", target = "ubicacion")
    @Mapping(source = "workDay", target = "jornada")
    @Mapping(source = "modality", target = "modalidad")
    @Mapping(source = "typeApplication", target = "tipo")
    @Mapping(source = "salaryMin", target = "salarioMin")
    @Mapping(source = "salaryMax", target = "salarioMax")
    @Mapping(source = "effectiveStatus", target = "estado")
    @Mapping(source = "publicationDate", target = "fechaPublicacion")
    @Mapping(source = "closingDate", target = "fechaCierre")
    @Mapping(source = "createdAt", target = "creada")
    @Mapping(source = "updatedAt", target = "actualizada")
    @Mapping(source = "careers", target = "carreras")
    @Mapping(source = "technologies", target = "tecnologias")
    @Mapping(source = "corporatePartnershipsUAEMex", target = "empresa")
    @Mapping(source = "recruiter", target = "reclutador")
    PostJobDto toDto(PostJob postJob);

    List<PostJobDto> toDtoList(List<PostJob> postJobs);

    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "lastName", target = "apellido")
    @Mapping(source = "position", target = "puesto")
    @Mapping(source = "image", target = "imagen")
    JobRecruiterDto toJobRecruiterDto(Recruiter recruiter);
}
