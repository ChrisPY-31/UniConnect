package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.ProjectLinkMappers;
import com.chris.uniconnect.Model.Dto.ProjectLinksDto;
import com.chris.uniconnect.Model.Entity.Project;
import com.chris.uniconnect.Model.Entity.ProjectLink;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Repository.ProjectLinkRepository;
import com.chris.uniconnect.Repository.ProjectRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Service.IProjectLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectLinkServiceImpl implements IProjectLinkService {

    @Autowired
    private ProjectLinkRepository projectLinkRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public ProjectLinksDto createProjectLinks(String username, ProjectLinksDto projectLinksDto) {
        Student owner = requireStudent(username);
        requireOwnedProject(owner, projectLinksDto.getIdProyecto());

        return ProjectLinkMappers.INSTANCE.ProjectLinkToProjectLinksDto(
                projectLinkRepository.save(ProjectLinkMappers.INSTANCE.ProjectLinksDtoToProjectLink(projectLinksDto)));
    }

    @Override
    public ProjectLinksDto updateProjectLinks(String username, Integer id, ProjectLinksDto projectLinksDto) {
        Student owner = requireStudent(username);

        ProjectLink existing = projectLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Red de proyecto", "id", id));

        requireOwnedProject(owner, existing.getIdProject());

        if (projectLinksDto.getIdProyecto() != null) {
            requireOwnedProject(owner, projectLinksDto.getIdProyecto());
        }

        ProjectLinkMappers.INSTANCE.updateProjectLinkFromDto(projectLinksDto, existing);
        return ProjectLinkMappers.INSTANCE.ProjectLinkToProjectLinksDto(projectLinkRepository.save(existing));
    }

    @Override
    public ProjectLinksDto getProjectLinkById(Integer id) {
        return ProjectLinkMappers.INSTANCE.ProjectLinkToProjectLinksDto(
                projectLinkRepository.findById(id).orElse(null));
    }

    @Override
    public void deleteProjectLinks(String username, Integer id) {
        Student owner = requireStudent(username);

        ProjectLink existing = projectLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Red de proyecto", "id", id));

        requireOwnedProject(owner, existing.getIdProject());

        projectLinkRepository.delete(existing);
    }

    @Override
    public boolean existsProjectLinks(Integer id) {
        return projectLinkRepository.existsById(id);
    }

    private Student requireStudent(String username) {
        Student student = studentRepository.findByUserEntityUsername(username);
        if (student == null) {
            throw new ResourceNotFoundException("Estudiante", "username", username);
        }
        return student;
    }

    private void requireOwnedProject(Student owner, Integer idProject) {
        Project project = projectRepository.findById(idProject)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto", "id", idProject));

        if (!owner.getId().equals(project.getIdStudent())) {
            throw new BadRequestException("No tienes permiso sobre este proyecto");
        }
    }
}
