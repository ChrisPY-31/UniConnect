package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.CorporatePartnershipsUAEMexDto;
import com.chris.uniconnect.Model.Dto.Response.PartnershipOptionResponse;
import com.chris.uniconnect.Model.Dto.Response.PartnershipStatsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICorporatePartnershipsService {

    CorporatePartnershipsUAEMexDto createPartnership(CorporatePartnershipsUAEMexDto partnership);

    Page<CorporatePartnershipsUAEMexDto> getPartnerships(Boolean vigente, String text, Pageable pageable);

    PartnershipStatsResponse getPartnershipStats(int expiringDays);

    List<PartnershipOptionResponse> getPartnershipOptions(boolean withoutCompanyAdmin);

    CorporatePartnershipsUAEMexDto getPartnershipById(Long idPartnership);

    CorporatePartnershipsUAEMexDto updatePartnership(Long idPartnership, CorporatePartnershipsUAEMexDto partnership);

    CorporatePartnershipsUAEMexDto changeStatus(Long idPartnership, boolean active);
}
