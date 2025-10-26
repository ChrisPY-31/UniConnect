package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;

public interface IRecruiterServce {

    RecruiterDto getRecruiterById(int id);

    RecruiterDto createRecruiter(RecruiterDto recruiter);

    boolean existRecruiter(int id);
}
