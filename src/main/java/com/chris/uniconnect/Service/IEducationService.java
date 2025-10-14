package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.EducationDto;

public interface IEducationService {

    EducationDto create(EducationDto educationDto);

    EducationDto updateEducation(EducationDto educationDto);

    Boolean exitEducation(Integer educationId);

    void deleteEducation(int educationId);
}
