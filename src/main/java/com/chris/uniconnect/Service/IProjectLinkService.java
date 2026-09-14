package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.ProjectLinksDto;

public interface IProjectLinkService {

    ProjectLinksDto createProjectLinks(String username, ProjectLinksDto projectLinksDto);

    ProjectLinksDto updateProjectLinks(String username, Integer id, ProjectLinksDto projectLinksDto);

    ProjectLinksDto getProjectLinkById(Integer id);

    void deleteProjectLinks(String username, Integer id);

    boolean existsProjectLinks(Integer id);
}
