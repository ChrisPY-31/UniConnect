package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.IdiomasMappers;
import com.chris.uniconnect.Model.Dto.LanguageDto;
import com.chris.uniconnect.Model.Entity.Language;
import com.chris.uniconnect.Repository.LanguageRepository;
import com.chris.uniconnect.Service.ILanguageService;
import com.chris.uniconnect.Service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements ILanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private IPersonService personService;

    @Override
    public List<LanguageDto> getMyLanguages(String username) {
        Integer personId = personService.getPersonByUserName(username).getId();
        return IdiomasMappers.INSTANCE.LanguageListtoLanguageDtoList(languageRepository.findByIdPerson(personId));
    }

    @Override
    public List<LanguageDto> createLanguage(String username, List<LanguageDto> languageDto) {
        Integer personId = personService.getPersonByUserName(username).getId();

        for (LanguageDto dto : languageDto) {
            dto.setIdPersona(personId);
        }

        List<Language> saved = languageRepository.saveAll(IdiomasMappers.INSTANCE.LanguageDtoListtoLangugeList(languageDto));
        return IdiomasMappers.INSTANCE.LanguageListtoLanguageDtoList(saved);
    }

    @Override
    public LanguageDto updateLanguage(String username, Integer id, LanguageDto languageDto) {
        Integer personId = personService.getPersonByUserName(username).getId();

        Language existing = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Idioma", "id", id));

        if (!personId.equals(existing.getIdPerson())) {
            throw new BadRequestException("No tienes permiso para modificar este idioma");
        }

        languageDto.setIdPersona(personId);
        IdiomasMappers.INSTANCE.updateLanguageFromDto(languageDto, existing);
        return IdiomasMappers.INSTANCE.languageToLanguageDto(languageRepository.save(existing));
    }

    @Override
    public void deleteLanguage(String username, Integer id) {
        Integer personId = personService.getPersonByUserName(username).getId();

        Language existing = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Idioma", "id", id));

        if (!personId.equals(existing.getIdPerson())) {
            throw new BadRequestException("No tienes permiso para eliminar este idioma");
        }

        languageRepository.delete(existing);
    }
}
