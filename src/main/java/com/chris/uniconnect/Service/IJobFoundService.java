package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.JobFoundDto;

public interface IJobFoundService {


    JobFoundDto createFoundDto(JobFoundDto jobFoundDto);

    void deleteFoundDto(JobFoundDto jobFoundDto);

}
