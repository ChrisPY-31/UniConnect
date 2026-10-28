package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.CorporatePartnershipsUAEMexDto;

import java.util.List;

public interface ICorporatePartnershipsService {

    CorporatePartnershipsUAEMexDto createPartnership(CorporatePartnershipsUAEMexDto partnership);

    List<CorporatePartnershipsUAEMexDto> getPartnerships(Boolean vigente);

    CorporatePartnershipsUAEMexDto getPartnershipById(Long idPartnership);

    CorporatePartnershipsUAEMexDto updatePartnership(Long idPartnership, CorporatePartnershipsUAEMexDto partnership);

    CorporatePartnershipsUAEMexDto changeStatus(Long idPartnership, boolean active);
}
