package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.CreateRecruiterRequest;
import com.chris.uniconnect.Model.Dto.RecruiterAccountDto;

import java.util.List;

public interface IRecruiterAccountService {

    //COMPANY_ADMIN: siempre sobre los reclutadores de su propio convenio
    RecruiterAccountDto createRecruiter(String companyAdminUsername, CreateRecruiterRequest request);

    List<RecruiterAccountDto> getMyRecruiters(String companyAdminUsername, Boolean activo);

    RecruiterAccountDto getMyRecruiter(String companyAdminUsername, Integer idRecruiter);

    RecruiterAccountDto changeMyRecruiterStatus(String companyAdminUsername, Integer idRecruiter, boolean active);

    void resetMyRecruiterPassword(String companyAdminUsername, Integer idRecruiter);

    //ADMIN: lectura por convenio y desactivacion como control
    List<RecruiterAccountDto> getRecruitersByPartnership(Long idPartnership);

    RecruiterAccountDto changeRecruiterStatus(Integer idRecruiter, boolean active);
}
