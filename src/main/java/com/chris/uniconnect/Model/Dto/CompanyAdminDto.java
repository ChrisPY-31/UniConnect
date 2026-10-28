package com.chris.uniconnect.Model.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAdminDto extends PersonDto {

    private Long idCorporacion;

    private String puesto;

    private String username;

    private String email;

    private Boolean cuentaActiva;

    private CorporatePartnershipsUAEMexDto empresa;
}
