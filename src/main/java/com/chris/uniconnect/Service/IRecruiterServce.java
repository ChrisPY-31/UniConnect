package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Dto.RecruiterProfileRequest;

public interface IRecruiterServce {

    //el reclutador siempre se resuelve por el username del JWT
    RecruiterDto getMyProfile(String username);

    RecruiterDto updateMyProfile(String username, RecruiterProfileRequest request);
}
