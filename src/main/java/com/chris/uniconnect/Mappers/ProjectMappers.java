package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Entity.Project;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectMappers {

    ProjectMappers INSTANCE = Mappers.getMapper(ProjectMappers.class);

    @Mapping(source = "idProject", target = "id")
    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "image", target = "imagen")
    @Mapping(source = "startDate", target = "fechaInicio")
    @Mapping(source = "endDate", target = "fechaFin")
    ProjectDto ProjectToProjectDto(Project project);

    @InheritInverseConfiguration
    Project ProjectDtoToProject(ProjectDto projectDto);


}
