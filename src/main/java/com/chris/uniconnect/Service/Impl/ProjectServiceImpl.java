package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.ProjectMappers;
import com.chris.uniconnect.Mappers.StudentMappers;
import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Repository.ProjectRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private StudentRepository studentRepository;

    @Override
    public List<ProjectDto> getAllProjects() {
        return ProjectMappers.INSTANCE.listEntityToListProjectDto(projectRepository.findAll());
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        return ProjectMappers.INSTANCE.ProjectToProjectDto(projectRepository.save(ProjectMappers.INSTANCE.ProjectDtoToProject(projectDto)));
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto) {
        return ProjectMappers.INSTANCE.ProjectToProjectDto(projectRepository.save(ProjectMappers.INSTANCE.ProjectDtoToProject(projectDto)));
    }

    @Override
    public void deleteProject(ProjectDto projectDto) {
        projectRepository.delete(ProjectMappers.INSTANCE.ProjectDtoToProject(projectDto));
    }

    @Override
    public Boolean existBoolean(Integer idProject) {
        return projectRepository.existsById(idProject);
    }

    @Override
    public ProjectDto getProject(Integer idProject) {
        return ProjectMappers.INSTANCE.ProjectToProjectDto(projectRepository.findById(idProject).orElse(null));
    }
}
