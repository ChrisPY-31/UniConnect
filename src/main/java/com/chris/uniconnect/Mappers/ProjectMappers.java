package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Entity.Project;
import com.chris.uniconnect.Model.Entity.Student;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {
        StudentMappers.class,
        ProjectLinkMappers.class,
        TechnologyMappers.class,
        TechnologyMappers.class
})
public interface ProjectMappers {

    ProjectMappers INSTANCE = Mappers.getMapper(ProjectMappers.class);

    @Mapping(source = "idProject", target = "idProject")
    @Mapping(source = "idStudent", target = "idEstudiante")
    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "image", target = "imagen")
    @Mapping(source = "startDate", target = "fechaInicio")
    @Mapping(source = "endDate", target = "fechaFin")
    @Mapping(source = "gitHub", target = "github")
    @Mapping(source = "deploy", target = "deploy")
    @Mapping(source = "mentions", target = "menciones")
    @Mapping(source = "technologies", target = "tecnologias")
    ProjectDto ProjectToProjectDto(Project project);

    @InheritInverseConfiguration
    Project ProjectDtoToProject(ProjectDto projectDto);

    List<ProjectDto> listEntityToListProjectDto(List<Project> projects);

}
