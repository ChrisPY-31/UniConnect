package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.JobFoundDto;
import com.chris.uniconnect.Model.Entity.JobFound;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        StudentMappers.class,
        RecruiterMappers.class
})
public interface JobFoundMappers {

    JobFoundMappers INSTANCE = Mappers.getMapper(JobFoundMappers.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "contractDate", target = "fechaContratacion")
    @Mapping(source = "position", target = "posicion")
    @Mapping(source = "student", target = "estudiante")
    @Mapping(source = "recruiter", target = "reclutador")
    @Mapping(source = "company", target = "compania")
    JobFoundDto jobFoundToJobFoundDto(JobFound jobFound);

    @InheritInverseConfiguration
    JobFound jobFoundDtoToJobFound(JobFoundDto jobFoundDto);

}
