package com.chris.uniconnect.Model.Dto.Response;

import com.chris.uniconnect.Model.Dto.*;
import lombok.Data;

import java.util.List;

@Data
public class PersonResponse {
    private Integer id;

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
