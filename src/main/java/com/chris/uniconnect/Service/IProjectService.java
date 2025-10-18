package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.ProjectDto;

import java.util.List;

public interface IProjectService {

    List<ProjectDto> getAllProjects();

    ProjectDto createProject(ProjectDto projectDto);

    ProjectDto updateProject(ProjectDto projectDto);

    void deleteProject(ProjectDto projectDto);

    Boolean existBoolean(Integer idProject);

    ProjectDto getProject(Integer idProject);

}
