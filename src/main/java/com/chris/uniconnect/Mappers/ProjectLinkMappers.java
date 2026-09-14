package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.ProjectLinksDto;
import com.chris.uniconnect.Model.Entity.Project;
import com.chris.uniconnect.Model.Entity.ProjectLink;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectLinkMappers {

    ProjectLinkMappers INSTANCE = Mappers.getMapper(ProjectLinkMappers.class);

    @Mapping(source = "idProjectLink", target = "id")
    @Mapping(source = "idProject", target = "idProyecto")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "projectLink", target = "redNombre")
    ProjectLinksDto ProjectLinkToProjectLinksDto(ProjectLink projectLink);

    @InheritInverseConfiguration
    ProjectLink ProjectLinksDtoToProjectLink(ProjectLinksDto ProjectLinkDto);

    @InheritConfiguration(name = "ProjectLinksDtoToProjectLink")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectLinkFromDto(ProjectLinksDto projectLinkDto, @MappingTarget ProjectLink projectLink);
}
