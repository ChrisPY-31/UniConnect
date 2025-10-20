package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Dto.Response.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public abstract class PersonDto {

    private Integer id;

    private Integer idUbicacion;

    private String nombre;

    private String apellido;

    private String descripcion;

    private String fechaNacimiento;

    private String imagen;

    private String especialidad;

    private String curriculum;

    private List<AptitudeResponse> aptitudes;

    private UbicationDto ubicacion;

    private List<LanguageResponse> idiomas;

    private List<EducationResponse> educaciones;

    private List<RedesContactoResponse> redContactos;

}
