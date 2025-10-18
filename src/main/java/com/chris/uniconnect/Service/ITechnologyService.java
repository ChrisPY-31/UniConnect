package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.TechnologyDto;

import java.util.List;

public interface ITechnologyService {

    boolean existTechnology(Integer id);

    List<TechnologyDto> createTechnology(List<TechnologyDto> technologyDto);

    void deleteTechnology(Integer id);
}
