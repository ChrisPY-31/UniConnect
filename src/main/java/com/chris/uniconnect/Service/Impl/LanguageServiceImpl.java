package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.IdiomasMappers;
import com.chris.uniconnect.Model.Dto.LanguageDto;
import com.chris.uniconnect.Repository.LanguageRepository;
import com.chris.uniconnect.Service.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements ILanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    @Override
    public List<LanguageDto> getAllLanguages() {
        return IdiomasMappers.INSTANCE.LanguageListtoLanguageDtoList(languageRepository.findAll());
    }

    @Override
    public LanguageDto createLanguage(LanguageDto languageDto) {
        return IdiomasMappers.INSTANCE.languageToLanguageDto(languageRepository.save(IdiomasMappers.INSTANCE.languageDtoLanguage(languageDto)));
    }

    @Override
    public void deleteLanguage(LanguageDto languageDto) {
        languageRepository.delete(IdiomasMappers.INSTANCE.languageDtoLanguage(languageDto));
    }

    @Override
    public LanguageDto getLanguageById(Integer id) {
        return languageRepository.findById(id).map(IdiomasMappers.INSTANCE::languageToLanguageDto).orElse(null);
    }

    @Override
    public boolean languageExists(Integer id) {
        return languageRepository.existsById(id);
    }
}
