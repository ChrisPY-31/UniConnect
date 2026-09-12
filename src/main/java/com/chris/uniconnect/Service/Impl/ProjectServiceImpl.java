package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.ProjectMappers;
import com.chris.uniconnect.Mappers.StudentMappers;
import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Entity.Project;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Repository.ProjectRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Service.INotificationService;
import com.chris.uniconnect.Service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private INotificationService notificationService;

    private StudentRepository studentRepository;

    @Override
    public List<ProjectDto> getAllProjects() {
        return ProjectMappers.INSTANCE.listEntityToListProjectDto(projectRepository.findAll());
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Project savedProject = projectRepository.save(ProjectMappers.INSTANCE.ProjectDtoToProject(projectDto));

        notifyMentions(savedProject, mentionedIds(savedProject));

        return ProjectMappers.INSTANCE.ProjectToProjectDto(savedProject);
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto) {
        Set<Integer> previousMentions = projectRepository.findById(projectDto.getIdProject())
                .map(this::mentionedIds)
                .orElseGet(HashSet::new);

        Project savedProject = projectRepository.save(ProjectMappers.INSTANCE.ProjectDtoToProject(projectDto));

        Set<Integer> newMentions = mentionedIds(savedProject);
        newMentions.removeAll(previousMentions);

        notifyMentions(savedProject, newMentions);

        return ProjectMappers.INSTANCE.ProjectToProjectDto(savedProject);
    }

    private Set<Integer> mentionedIds(Project project) {
        if (project.getMentions() == null) {
            return new HashSet<>();
        }
        Set<Integer> ids = new HashSet<>();
        for (Student student : project.getMentions()) {
            ids.add(student.getId());
        }
        return ids;
    }

    private void notifyMentions(Project project, Set<Integer> studentIds) {
        for (Integer studentId : studentIds) {
            notificationService.notifyProjectMention(studentId, project.getIdProject(), project.getName());
        }
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
