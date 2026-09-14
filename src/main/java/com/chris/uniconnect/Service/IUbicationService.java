package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.UbicationDto;

import java.util.List;

public interface IUbicationService {

    List<UbicationDto> getUbications();

    List<UbicationDto> createUbication(List<UbicationDto> ubicationDto);

    boolean existUbication(Integer id);

    void deleteUbication(Integer id);
}
