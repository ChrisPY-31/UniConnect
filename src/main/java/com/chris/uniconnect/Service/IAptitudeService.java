package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.AptitudeDto;

import java.util.List;

public interface IAptitudeService {

    List<AptitudeDto> createAptitud(List<AptitudeDto> aptitudeDto);

    void deleteAptitude(AptitudeDto aptitudeDto);


}
