package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.Response.SkillResponse;

import java.util.List;

public interface ISkillService {

    List<SkillResponse> getCatalog();
}
