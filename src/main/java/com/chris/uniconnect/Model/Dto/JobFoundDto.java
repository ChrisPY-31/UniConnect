package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Entity.JobFoundPk;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobFoundDto {

    private JobFoundPk id;

    private LocalDate fechaContratacion;

    private String posicion;

    private StudentDto estudiante;

    private RecruiterDto reclutador;

    private CompanyDto compania;
}
