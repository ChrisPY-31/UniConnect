package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.LanguageDto;

import java.util.List;

public interface ILanguageService {

    List<LanguageDto> getMyLanguages(String username);

    List<LanguageDto> createLanguage(String username, List<LanguageDto> languageDto);

    LanguageDto updateLanguage(String username, Integer id, LanguageDto languageDto);

    void deleteLanguage(String username, Integer id);
}
