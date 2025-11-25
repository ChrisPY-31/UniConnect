package com.chris.uniconnect.Service.Impl;


import com.chris.uniconnect.Model.Dto.Response.DashBoardResponse;
import com.chris.uniconnect.Repository.PersonaRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DashboardServiceImpl {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PersonaRepository personaRepository;


    public DashBoardResponse dashBoardResponse() {
        long totalUsuarios = personaRepository.count();
        long software = studentRepository.countByCareer_CareerName("Software");
        long computacion = studentRepository.countByCareer_CareerName("Computacion");
        long ciberseguridad = studentRepository.countByCareer_CareerName("Ciberseguridad");
        DashBoardResponse dash = new DashBoardResponse(totalUsuarios, software, computacion, ciberseguridad);
        return dash;
    }

}
