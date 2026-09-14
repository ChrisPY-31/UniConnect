package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.EducationDto;

public interface IEducationService {

    EducationDto create(String username, EducationDto educationDto);

    EducationDto updateEducation(String username, Integer idEducation, EducationDto educationDto);

    void deleteEducation(String username, Integer educationId);
}
