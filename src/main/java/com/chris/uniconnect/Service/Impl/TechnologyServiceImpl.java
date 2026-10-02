package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.TechnologyMappers;
import com.chris.uniconnect.Model.Dto.TechnologyDto;
import com.chris.uniconnect.Model.Entity.Technology;
import com.chris.uniconnect.Repository.TechnologyRepository;
import com.chris.uniconnect.Service.ITechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TechnologyServiceImpl implements ITechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;

    @Override
    public boolean existTechnology(Integer id) {
        return technologyRepository.existsById(id);
    }

    @Override
    public List<TechnologyDto> getTechnologies() {
        return TechnologyMappers.INSTANCE.listTechnologyToListTechnonlogyDto(technologyRepository.findAll());
    }

    @Override
    public List<TechnologyDto> createTechnology(List<TechnologyDto> technologyDto) {
        Set<String> vistos = new HashSet<>();
        List<Technology> nuevasTecnologias = technologyDto.stream()
                .filter(dto -> dto.getNombre() != null && !dto.getNombre().isBlank())
                .peek(dto -> dto.setNombre(dto.getNombre().trim()))
                .filter(dto -> vistos.add(dto.getNombre().toLowerCase()))
                .filter(dto -> !technologyRepository.existsByNameIgnoreCase(dto.getNombre()))
                .map(TechnologyMappers.INSTANCE::DtoToTechnology)
                .toList();

        return TechnologyMappers.INSTANCE.listTechnologyToListTechnonlogyDto(technologyRepository.saveAll(nuevasTecnologias));
    }

    @Override
    public TechnologyDto updateTechnology(Integer id, TechnologyDto technologyDto) {
        Technology existingTechnology = technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology", "idTecnologia", id));

        String nombre = technologyDto.getNombre() == null ? "" : technologyDto.getNombre().trim();
        if (nombre.isBlank()) {
            throw new BadRequestException("El nombre de la tecnologia es obligatorio");
        }
        if (technologyRepository.existsByNameIgnoreCaseAndIdTechnologyNot(nombre, id)) {
            throw new BadRequestException("Ya existe una tecnologia con el nombre: " + nombre);
        }

        existingTechnology.setName(nombre);
        return TechnologyMappers.INSTANCE.tecnologyToDto(technologyRepository.save(existingTechnology));
    }

    @Override
    public void deleteTechnology(Integer id) {
        technologyRepository.deleteById(id);
    }
}
