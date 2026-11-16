package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.CompanyProfileDto;
import com.chris.uniconnect.Model.Dto.CompanyProfileRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICompanyProfileService {

    void createEmptyProfile(Long idPartnership);

    Page<CompanyProfileDto> getPublicProfiles(String text, Pageable pageable);

    CompanyProfileDto getPublicProfile(Long idPartnership);

    CompanyProfileDto getMyProfile(String username);

    CompanyProfileDto updateMyProfile(String username, CompanyProfileRequest request);

    CompanyProfileDto uploadMyLogo(String username, MultipartFile file);

    CompanyProfileDto uploadMyBanner(String username, MultipartFile file);
}
