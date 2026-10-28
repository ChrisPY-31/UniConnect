package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.CorporatePartnershipsMappers;
import com.chris.uniconnect.Model.Dto.CorporatePartnershipsUAEMexDto;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import com.chris.uniconnect.Repository.CorporatePartnershipsRepository;
import com.chris.uniconnect.Service.ICorporatePartnershipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorporatePartnershipsServiceImpl implements ICorporatePartnershipsService {

    @Autowired
    private CorporatePartnershipsRepository partnershipsRepository;

    @Override
    public CorporatePartnershipsUAEMexDto createPartnership(CorporatePartnershipsUAEMexDto partnership) {
        String name = partnership.getNombreEmpresa().trim();
        if (partnershipsRepository.existsByCompanyNameIgnoreCase(name)) {
            throw new BadRequestException("Ya existe un convenio con la empresa: " + name);
        }
        validateDates(partnership);

        CorporatePartnershipsUAEMex newPartnership = CorporatePartnershipsMappers.INSTANCE.toEntity(partnership);
        newPartnership.setCompanyName(name);
        return CorporatePartnershipsMappers.INSTANCE.toDto(partnershipsRepository.save(newPartnership));
    }

    @Override
    public List<CorporatePartnershipsUAEMexDto> getPartnerships(Boolean vigente) {
        List<CorporatePartnershipsUAEMex> partnerships = partnershipsRepository.findAll();
        if (vigente != null) {
            partnerships = partnerships.stream()
                    .filter(partnership -> partnership.isVigente() == vigente)
                    .toList();
        }
        return CorporatePartnershipsMappers.INSTANCE.toDtoList(partnerships);
    }

    @Override
    public CorporatePartnershipsUAEMexDto getPartnershipById(Long idPartnership) {
        return CorporatePartnershipsMappers.INSTANCE.toDto(findPartnership(idPartnership));
    }

    @Override
    public CorporatePartnershipsUAEMexDto updatePartnership(Long idPartnership, CorporatePartnershipsUAEMexDto partnership) {
        CorporatePartnershipsUAEMex existingPartnership = findPartnership(idPartnership);

        String name = partnership.getNombreEmpresa().trim();
        if (partnershipsRepository.existsByCompanyNameIgnoreCaseAndIdCorporatePartnershipNot(name, idPartnership)) {
            throw new BadRequestException("Ya existe un convenio con la empresa: " + name);
        }
        validateDates(partnership);

        CorporatePartnershipsMappers.INSTANCE.updateFromDto(partnership, existingPartnership);
        existingPartnership.setCompanyName(name);
        return CorporatePartnershipsMappers.INSTANCE.toDto(partnershipsRepository.save(existingPartnership));
    }

    @Override
    public CorporatePartnershipsUAEMexDto changeStatus(Long idPartnership, boolean active) {
        CorporatePartnershipsUAEMex partnership = findPartnership(idPartnership);
        partnership.setActive(active);
        return CorporatePartnershipsMappers.INSTANCE.toDto(partnershipsRepository.save(partnership));
    }

    private CorporatePartnershipsUAEMex findPartnership(Long idPartnership) {
        return partnershipsRepository.findById(idPartnership)
                .orElseThrow(() -> new ResourceNotFoundException("Convenio", "idCorporacion", idPartnership));
    }

    private void validateDates(CorporatePartnershipsUAEMexDto partnership) {
        if (partnership.getFechaFinal() != null && partnership.getFechaFinal().isBefore(partnership.getFechaInicio())) {
            throw new BadRequestException("La fecha final no puede ser anterior a la fecha de inicio");
        }
    }
}
