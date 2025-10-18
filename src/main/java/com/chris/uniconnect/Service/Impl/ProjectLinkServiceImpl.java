package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.ProjectLinkMappers;
import com.chris.uniconnect.Model.Dto.ProjectLinksDto;
import com.chris.uniconnect.Repository.ProjectLinkRepository;
import com.chris.uniconnect.Service.IProjectLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectLinkServiceImpl implements IProjectLinkService {

    @Autowired
    private ProjectLinkRepository projectLinkRepository;

    @Override
    public ProjectLinksDto createProjectLinks(ProjectLinksDto projectLinksDto) {
        return ProjectLinkMappers.INSTANCE.ProjectLinkToProjectLinksDto(projectLinkRepository.save(ProjectLinkMappers.INSTANCE.ProjectLinksDtoToProjectLink(projectLinksDto)));
    }

    @Override
    public ProjectLinksDto getProjectLinkById(Integer id) {
        return ProjectLinkMappers.INSTANCE.ProjectLinkToProjectLinksDto(
                projectLinkRepository.findById(id).orElse(null));
    }

    @Override
    public void deleteProjectLinks(ProjectLinksDto projectLinksDto) {
        projectLinkRepository.delete(ProjectLinkMappers.INSTANCE.ProjectLinksDtoToProjectLink(projectLinksDto));
    }

    @Override
    public boolean existsProjectLinks(Integer id) {
        return projectLinkRepository.existsById(id);
    }
}
