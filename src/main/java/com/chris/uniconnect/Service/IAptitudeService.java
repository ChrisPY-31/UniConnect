package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.AptitudeDto;

import java.util.List;

public interface IAptitudeService {

    List<AptitudeDto> getAptitudes();

    List<AptitudeDto> createAptitud(List<AptitudeDto> aptitudeDto);

    boolean existAptitude(Integer id);

    void deleteAptitude(Integer id);

}
