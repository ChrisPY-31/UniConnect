package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.EducationMappers;
import com.chris.uniconnect.Model.Dto.EducationDto;
import com.chris.uniconnect.Model.Entity.Education;
import com.chris.uniconnect.Repository.EducationRepository;
import com.chris.uniconnect.Service.IEducationService;
import com.chris.uniconnect.Service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationServiceImpl implements IEducationService {

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private IPersonService personService;

    @Override
    public EducationDto create(String username, EducationDto educationDto) {
        Integer personId = personService.getPersonByUserName(username).getId();
        educationDto.setIdPersona(personId);

        Education saved = educationRepository.save(EducationMappers.INSTANCE.educationDtoToEducation(educationDto));
        return EducationMappers.INSTANCE.educationToEducationDto(saved);
    }

    @Override
    public EducationDto updateEducation(String username, Integer idEducation, EducationDto educationDto) {
        Integer personId = personService.getPersonByUserName(username).getId();

        Education existing = educationRepository.findById(idEducation)
                .orElseThrow(() -> new ResourceNotFoundException("Educacion", "id", idEducation));

        if (!personId.equals(existing.getIdPerson())) {
            throw new BadRequestException("No tienes permiso para modificar esta educacion");
        }

        educationDto.setIdPersona(personId);
        EducationMappers.INSTANCE.updateEducationFromDto(educationDto, existing);
        return EducationMappers.INSTANCE.educationToEducationDto(educationRepository.save(existing));
    }

    @Override
    public void deleteEducation(String username, Integer educationId) {
        Integer personId = personService.getPersonByUserName(username).getId();

        Education existing = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Educacion", "id", educationId));

        if (!personId.equals(existing.getIdPerson())) {
            throw new BadRequestException("No tienes permiso para eliminar esta educacion");
        }

        educationRepository.delete(existing);
    }
}
