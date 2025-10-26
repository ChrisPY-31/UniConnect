package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.RecruiterMappers;
import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Repository.RecruiterRepository;
import com.chris.uniconnect.Service.IRecruiterServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecruiterServiceImpl implements IRecruiterServce {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public RecruiterDto getRecruiterById(int id) {
        return RecruiterMappers.INSTANCE.recruiterToRecruiterDto(recruiterRepository.findById(id).orElse(null));
    }

    @Override
    public RecruiterDto createRecruiter(RecruiterDto recruiter) {
        return RecruiterMappers.INSTANCE.recruiterToRecruiterDto(recruiterRepository.save(RecruiterMappers.INSTANCE.recruiterDtoToRecruiter(recruiter)));
    }

    @Override
    public boolean existRecruiter(int id) {
        return recruiterRepository.existsById(id);
    }
}
