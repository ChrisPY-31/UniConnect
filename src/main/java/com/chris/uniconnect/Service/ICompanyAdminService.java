package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.CompanyAdminDto;
import com.chris.uniconnect.Model.Dto.CompanyAdminProfileRequest;
import com.chris.uniconnect.Model.Dto.CreateCompanyAdminRequest;

import java.util.List;

public interface ICompanyAdminService {

    CompanyAdminDto createCompanyAdmin(Long idPartnership, CreateCompanyAdminRequest request);

    List<CompanyAdminDto> getCompanyAdmins(Long idPartnership);

    CompanyAdminDto getCompanyAdminById(Integer idCompanyAdmin);

    CompanyAdminDto changeStatus(Integer idCompanyAdmin, boolean active);

    CompanyAdminDto getMyProfile(String username);

    CompanyAdminDto updateMyProfile(String username, CompanyAdminProfileRequest request);
}
