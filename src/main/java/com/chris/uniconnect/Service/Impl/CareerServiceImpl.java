package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.CareerMappers;
import com.chris.uniconnect.Model.Dto.CareerDto;
import com.chris.uniconnect.Model.Entity.Career;
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
        if (careerRepository.existsByCareerNameIgnoreCase(career.getCarrera())) {
            throw new BadRequestException("Ya existe una carrera con el nombre: " + career.getCarrera());
        }
        return CareerMappers.INSTANCE.carreerToCareerDto(careerRepository.save(CareerMappers.INSTANCE.careerDtoToCareer(career)));
    }

    @Override
    public CareerDto updateCareer(CareerDto career) {
        if (career.getIdCarrera() == null) {
            throw new BadRequestException("El id de la carrera es obligatorio");
        }
        Career existingCareer = careerRepository.findById(career.getIdCarrera())
                .orElseThrow(() -> new ResourceNotFoundException("Career", "idCarrera", career.getIdCarrera()));

        existingCareer.setCareerName(career.getCarrera());
        return CareerMappers.INSTANCE.carreerToCareerDto(careerRepository.save(existingCareer));
    }

    @Override
    public void deleteCareer(Integer idCareer) {
        if(!careerRepository.existsById(idCareer)){
            throw new ResourceNotFoundException("Career", "idCarrera", idCareer);
        }

        careerRepository.deleteById(idCareer);
    }


}
