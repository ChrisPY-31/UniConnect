package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.ProjectMappers;
import com.chris.uniconnect.Mappers.StudentMappers;
import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.StudentAllDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TechnologyDto;
import com.chris.uniconnect.Model.Entity.Project;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Repository.ProjectRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Repository.TechnologyRepository;
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

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Override
    public List<ProjectDto> getAllProjects() {
        return ProjectMappers.INSTANCE.listEntityToListProjectDto(projectRepository.findAll());
    }

    @Override
    public ProjectDto createProject(String username, ProjectDto projectDto) {
        Student owner = requireStudent(username);
        projectDto.setIdEstudiante(owner.getId());

        validateReferences(projectDto);

        Project savedProject = projectRepository.save(ProjectMappers.INSTANCE.ProjectDtoToProject(projectDto));

        notifyMentions(savedProject, mentionedIds(savedProject));

        return ProjectMappers.INSTANCE.ProjectToProjectDto(savedProject);
    }

    @Override
    public ProjectDto updateProject(String username, ProjectDto projectDto) {
        Student owner = requireStudent(username);

        Project existingProject = projectRepository.findById(projectDto.getIdProject())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", projectDto.getIdProject()));

        if (!owner.getId().equals(existingProject.getIdStudent())) {
            throw new BadRequestException("No tienes permiso para modificar este proyecto");
        }

        projectDto.setIdEstudiante(owner.getId());
        validateReferences(projectDto);

        Set<Integer> previousMentions = mentionedIds(existingProject);

        Project savedProject = projectRepository.save(ProjectMappers.INSTANCE.ProjectDtoToProject(projectDto));

        Set<Integer> newMentions = mentionedIds(savedProject);
        newMentions.removeAll(previousMentions);

        notifyMentions(savedProject, newMentions);

        return ProjectMappers.INSTANCE.ProjectToProjectDto(savedProject);
    }

    private Student requireStudent(String username) {
        Student student = studentRepository.findByUserEntityUsername(username);
        if (student == null) {
            throw new ResourceNotFoundException("Estudiante", "username", username);
        }
        return student;
    }

    private void validateReferences(ProjectDto projectDto) {
        if (projectDto.getIdEstudiante() == null) {
            throw new BadRequestException("El proyecto debe tener un estudiante asociado");
        }
        if (!studentRepository.existsById(projectDto.getIdEstudiante())) {
            throw new ResourceNotFoundException("Estudiante", "id", projectDto.getIdEstudiante());
        }

        if (projectDto.getMenciones() != null) {
            for (StudentAllDto mencion : projectDto.getMenciones()) {
                if (mencion.getId() == null || !studentRepository.existsById(mencion.getId())) {
                    throw new ResourceNotFoundException("Estudiante mencionado", "id", mencion.getId());
                }
            }
        }

        if (projectDto.getTecnologias() != null) {
            for (TechnologyDto tecnologia : projectDto.getTecnologias()) {
                if (tecnologia.getIdTecnologia() == null || !technologyRepository.existsById(tecnologia.getIdTecnologia())) {
                    throw new ResourceNotFoundException("Tecnologia", "id", tecnologia.getIdTecnologia());
                }
            }
        }
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
    public void deleteProject(String username, Integer idProject) {
        Student owner = requireStudent(username);

        Project existingProject = projectRepository.findById(idProject)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", idProject));

        if (!owner.getId().equals(existingProject.getIdStudent())) {
            throw new BadRequestException("No tienes permiso para eliminar este proyecto");
        }

        projectRepository.delete(existingProject);
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
