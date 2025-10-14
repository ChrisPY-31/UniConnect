package com.chris.uniconnect.Service;


import com.chris.uniconnect.Model.Dto.CareerDto;

import java.util.List;

public interface ICareerService {

    List<CareerDto> getCareers();

    CareerDto saveCareer(CareerDto career);
}
