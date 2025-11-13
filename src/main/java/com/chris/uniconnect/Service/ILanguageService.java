package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.LanguageDto;

import java.util.List;

public interface ILanguageService {

    List<LanguageDto> getAllLanguages();

    List<LanguageDto> createLanguage(List<LanguageDto> languageDto);

    void deleteLanguage(LanguageDto languageDto);

    LanguageDto getLanguageById(Integer id);

    boolean languageExists(Integer id);
}
