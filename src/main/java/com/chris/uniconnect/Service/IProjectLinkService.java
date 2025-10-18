package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.ProjectLinksDto;

public interface IProjectLinkService {

    ProjectLinksDto createProjectLinks(ProjectLinksDto projectLinksDto);

    ProjectLinksDto getProjectLinkById(Integer id);

    void deleteProjectLinks(ProjectLinksDto projectLinksDto);

    boolean existsProjectLinks(Integer id);
}
