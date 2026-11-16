package com.chris.uniconnect.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.chris.uniconnect.Model.Dto.CompanyAdminDto;
import com.chris.uniconnect.Model.Dto.CompanyAdminProfileRequest;
import com.chris.uniconnect.Model.Dto.CreateCompanyAdminRequest;

import java.util.List;

public interface ICompanyAdminService {

    CompanyAdminDto createCompanyAdmin(Long idPartnership, CreateCompanyAdminRequest request);

    Page<CompanyAdminDto> getCompanyAdmins(Long idPartnership, Boolean active, String text, Pageable pageable);

    CompanyAdminDto getCompanyAdminById(Integer idCompanyAdmin);

    CompanyAdminDto changeStatus(Integer idCompanyAdmin, boolean active);

    CompanyAdminDto getMyProfile(String username);

    CompanyAdminDto updateMyProfile(String username, CompanyAdminProfileRequest request);
}
