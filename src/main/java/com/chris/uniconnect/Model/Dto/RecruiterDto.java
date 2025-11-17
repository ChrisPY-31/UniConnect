package com.chris.uniconnect.Model.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterDto extends PersonDto{

    private Integer idCompania;

    private String posicion;

    private CompanyDto compania;
}
