package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.UbicationDto;

public interface IUbicationService {

    UbicationDto createUbication(UbicationDto ubicationDto);

    UbicationDto updateUbication(UbicationDto ubicationDto);

    void deleteUbication(Integer id);

    boolean existUbication(Integer id);
}
