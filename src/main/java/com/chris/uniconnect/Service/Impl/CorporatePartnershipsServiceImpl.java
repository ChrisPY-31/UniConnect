package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.CorporatePartnershipsMappers;
import com.chris.uniconnect.Model.Dto.CorporatePartnershipsUAEMexDto;
import com.chris.uniconnect.Model.Dto.Response.PartnershipOptionResponse;
import com.chris.uniconnect.Model.Dto.Response.PartnershipStatsResponse;
import com.chris.uniconnect.Repository.PartnershipSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import com.chris.uniconnect.Repository.CorporatePartnershipsRepository;
import com.chris.uniconnect.Service.ICompanyProfileService;
import com.chris.uniconnect.Service.ICorporatePartnershipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CorporatePartnershipsServiceImpl implements ICorporatePartnershipsService {

    @Autowired
    private CorporatePartnershipsRepository partnershipsRepository;

    @Autowired
    private ICompanyProfileService companyProfileService;

    @Override
    @Transactional
    public CorporatePartnershipsUAEMexDto createPartnership(CorporatePartnershipsUAEMexDto partnership) {
        String name = partnership.getNombreEmpresa().trim();
        if (partnershipsRepository.existsByCompanyNameIgnoreCase(name)) {
            throw new BadRequestException("Ya existe un convenio con la empresa: " + name);
        }
        validateDates(partnership);

        CorporatePartnershipsUAEMex newPartnership = CorporatePartnershipsMappers.INSTANCE.toEntity(partnership);
        newPartnership.setCompanyName(name);
        CorporatePartnershipsUAEMex savedPartnership = partnershipsRepository.save(newPartnership);
        //cada convenio nace con su landing vacia, la empresa solo la edita
        companyProfileService.createEmptyProfile(savedPartnership.getIdCorporatePartnership());
        return CorporatePartnershipsMappers.INSTANCE.toDto(savedPartnership);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CorporatePartnershipsUAEMexDto> getPartnerships(Boolean vigente, String text, Pageable pageable) {
        Specification<CorporatePartnershipsUAEMex> specification = Specification.allOf(
                PartnershipSpecifications.vigente(vigente),
                PartnershipSpecifications.text(text)
        );
        return partnershipsRepository.findAll(specification, pageable).map(CorporatePartnershipsMappers.INSTANCE::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PartnershipStatsResponse getPartnershipStats(int expiringDays) {
        long vigentes = partnershipsRepository.count(PartnershipSpecifications.vigente(true));
        long total = partnershipsRepository.count();
        return new PartnershipStatsResponse(total, vigentes, total - vigentes,
                partnershipsRepository.count(PartnershipSpecifications.expiringWithin(expiringDays)),
                partnershipsRepository.findDistinctCategories(),
                partnershipsRepository.findDistinctAgreementTypes());
    }

    //activos (los unicos a los que se les puede crear cuenta); con withoutCompanyAdmin, solo los que no tienen cuenta activa
    @Override
    @Transactional(readOnly = true)
    public List<PartnershipOptionResponse> getPartnershipOptions(boolean withoutCompanyAdmin) {
        Specification<CorporatePartnershipsUAEMex> specification = Specification.allOf(
                PartnershipSpecifications.active(),
                withoutCompanyAdmin ? PartnershipSpecifications.withoutActiveCompanyAdmin() : null
        );
        return partnershipsRepository.findAll(specification, Sort.by("companyName")).stream()
                .map(p -> new PartnershipOptionResponse(p.getIdCorporatePartnership(), p.getCompanyName()))
                .toList();
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
