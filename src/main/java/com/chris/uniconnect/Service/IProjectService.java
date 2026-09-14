package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.ProjectDto;

import java.util.List;

public interface IProjectService {

    List<ProjectDto> getAllProjects();

    ProjectDto createProject(String username, ProjectDto projectDto);

    ProjectDto updateProject(String username, ProjectDto projectDto);

    void deleteProject(String username, Integer idProject);

    Boolean existBoolean(Integer idProject);

    ProjectDto getProject(Integer idProject);

}
