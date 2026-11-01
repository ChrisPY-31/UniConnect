package com.chris.uniconnect.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//landing de la empresa: nombre y categoria vienen del convenio, lo demas del perfil
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfileDto {

    private Long idCorporacion;

    private String nombreEmpresa;

    private String categoria;

    private String descripcion;

    private String logo;

    private String banner;

    private String sitioWeb;

    private String sector;

    private String tamano;

    private String ubicacion;

    private String correoContacto;

    private LocalDateTime actualizado;
}
