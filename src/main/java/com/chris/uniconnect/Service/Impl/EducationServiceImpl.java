package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.EducationMappers;
import com.chris.uniconnect.Model.Dto.EducationDto;
import com.chris.uniconnect.Model.Entity.Education;
import com.chris.uniconnect.Repository.EducationRepository;
import com.chris.uniconnect.Service.IEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationServiceImpl implements IEducationService {

    @Autowired
    private EducationRepository educationRepository;

    @Override
    public EducationDto create(EducationDto educationDto) {
        return EducationMappers.INSTANCE.educationToEducationDto(educationRepository.save(EducationMappers.INSTANCE.educationDtoToEducation(educationDto)));
    }

    @Override
    public EducationDto updateEducation(EducationDto educationDto) {
        return null;
    }

    @Override
    public Boolean exitEducation(Integer educationId) {
        return educationRepository.existsById(educationId);
    }

    @Override
    public void deleteEducation(int educationId) {
        educationRepository.findById(educationId).ifPresent(education -> educationRepository.delete(education));
    }
}
