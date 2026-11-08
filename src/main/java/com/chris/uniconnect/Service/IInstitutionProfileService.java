package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.InstitutionProfileDto;
import com.chris.uniconnect.Model.Dto.InstitutionProfileRequest;
import com.chris.uniconnect.Model.Entity.InstitutionProfile;
import org.springframework.web.multipart.MultipartFile;

public interface IInstitutionProfileService {

    InstitutionProfile findOrCreate();

    InstitutionProfileDto getProfile();

    InstitutionProfileDto updateProfile(InstitutionProfileRequest request);

    InstitutionProfileDto uploadLogo(MultipartFile file);

    InstitutionProfileDto uploadBanner(MultipartFile file);
}
