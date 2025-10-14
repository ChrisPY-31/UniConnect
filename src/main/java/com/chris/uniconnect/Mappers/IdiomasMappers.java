package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.LanguageDto;
import com.chris.uniconnect.Model.Dto.Response.LanguageResponse;
import com.chris.uniconnect.Model.Entity.Language;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IdiomasMappers {

    IdiomasMappers INSTANCE = Mappers.getMapper(IdiomasMappers.class);

    @Mapping(source = "idLanguaje" , target = "idIdioma")
    @Mapping(source = "idPerson" , target = "idPersona")
    @Mapping(source = "name" , target = "nombre")
    @Mapping(source = "level" , target = "nivel")
    LanguageDto languageToLanguageDto(Language language);

    @InheritInverseConfiguration

    Language languageDtoLanguage(LanguageDto languageDto);

    List<LanguageDto> LanguageListtoLanguageDtoList(List<Language> languages);

    LanguageDto toDto (LanguageResponse languageResponse);

    LanguageResponse toEntity (LanguageDto languageDto);
}


