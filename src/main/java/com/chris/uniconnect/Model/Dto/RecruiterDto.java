package com.chris.uniconnect.Model.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterDto extends PersonDto{

    private Long idCorporacion;

    private String posicion;

    private CorporatePartnershipsUAEMexDto empresaAsociada;

}
