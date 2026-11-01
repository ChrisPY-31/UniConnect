package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.RecruiterMappers;
import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Dto.RecruiterProfileRequest;
import com.chris.uniconnect.Model.Entity.Recruiter;
import com.chris.uniconnect.Repository.RecruiterRepository;
import com.chris.uniconnect.Repository.UbicationRepository;
import com.chris.uniconnect.Service.IRecruiterServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecruiterServiceImpl implements IRecruiterServce {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UbicationRepository ubicationRepository;

    @Override
    @Transactional(readOnly = true)
    public RecruiterDto getMyProfile(String username) {
        return RecruiterMappers.INSTANCE.recruiterToRecruiterDto(findByUsername(username));
    }

    @Override
    @Transactional
    public RecruiterDto updateMyProfile(String username, RecruiterProfileRequest request) {
        Recruiter recruiter = findByUsername(username);

        if (request.idUbicacion() != null && !ubicationRepository.existsById(request.idUbicacion())) {
            throw new BadRequestException("No existe la ubicacion con id: " + request.idUbicacion());
        }

        recruiter.setName(request.nombre().trim());
        recruiter.setLastName(request.apellido().trim());
        recruiter.setPosition(request.puesto());
        recruiter.setDescription(request.descripcion());
        recruiter.setSpecialty(request.especialidad());
        recruiter.setCity(request.ciudad());
        recruiter.setIdUbication(request.idUbicacion());
        recruiter.setUbication(request.idUbicacion() == null ? null : ubicationRepository.getReferenceById(request.idUbicacion()));
        recruiter.setBirthDate(request.fechaNacimiento());
        return RecruiterMappers.INSTANCE.recruiterToRecruiterDto(recruiterRepository.save(recruiter));
    }

    private Recruiter findByUsername(String username) {
        Recruiter recruiter = recruiterRepository.findByUserEntityUsername(username);
        if (recruiter == null) {
            throw new ResourceNotFoundException("Reclutador", "username", username);
        }
        return recruiter;
    }
}
