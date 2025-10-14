package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Entity.Student;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CareerDto {

    private Integer idCarrera;

    private String carrera;


}
