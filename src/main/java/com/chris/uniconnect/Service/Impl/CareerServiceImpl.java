package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.CareerMappers;
import com.chris.uniconnect.Model.Dto.CareerDto;
import com.chris.uniconnect.Repository.CareerRepository;
import com.chris.uniconnect.Service.ICareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerServiceImpl implements ICareerService {

    @Autowired
    private CareerRepository careerRepository;

    @Override
    public List<CareerDto> getCareers() {
        return careerRepository.findAll().stream().map(CareerMappers.INSTANCE::carreerToCareerDto).collect(Collectors.toList());
    }

    @Override
    public CareerDto saveCareer(CareerDto career) {
        return CareerMappers.INSTANCE.carreerToCareerDto(careerRepository.save(CareerMappers.INSTANCE.careerDtoToCareer(career)));
    }


}
