package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Entity.JobFound;
import com.chris.uniconnect.Model.Entity.Recruiter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDto {
    private Integer idCompania;

    private String nombre;

    private String sector;

    private String locacion;

}
