package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.SkillMappers;
import com.chris.uniconnect.Model.Dto.Response.SkillResponse;
import com.chris.uniconnect.Repository.AptitudeRepository;
import com.chris.uniconnect.Repository.TechnologyRepository;
import com.chris.uniconnect.Service.ISkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements ISkillService {

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private AptitudeRepository aptitudeRepository;

    @Override
    public List<SkillResponse> getCatalog() {
        return SkillMappers.INSTANCE.combine(technologyRepository.findAll(), aptitudeRepository.findAll());
    }
}
