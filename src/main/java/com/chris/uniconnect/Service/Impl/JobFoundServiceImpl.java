package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.JobFoundMappers;
import com.chris.uniconnect.Model.Dto.JobFoundDto;
import com.chris.uniconnect.Repository.JobFoundRepository;
import com.chris.uniconnect.Service.IJobFoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobFoundServiceImpl implements IJobFoundService {

    @Autowired
    private JobFoundRepository jobFoundRepository;

    @Override
    public JobFoundDto createFoundDto(JobFoundDto jobFoundDto) {
        if (jobFoundRepository.existsById(jobFoundDto.getId().getIdStudent())) {
            throw new BadRequestException("El estudiante ya tiene empleo");
        }
        return JobFoundMappers.INSTANCE.jobFoundToJobFoundDto(jobFoundRepository.save(JobFoundMappers.INSTANCE.jobFoundDtoToJobFound(jobFoundDto)));
    }

    @Override
    public void deleteFoundDto(JobFoundDto jobFoundDto) {
        jobFoundRepository.delete(JobFoundMappers.INSTANCE.jobFoundDtoToJobFound(jobFoundDto));
    }
}
