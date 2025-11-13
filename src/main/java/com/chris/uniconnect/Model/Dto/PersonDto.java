package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Dto.Response.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private Integer id;

    private Integer idUbicacion;

    @NotBlank
    private String nombre;

    @NonNull
    private String apellido;

    private String descripcion;

    private String fechaNacimiento;

    private String imagen;

    private String especialidad;

    private String curriculum;

    private String tipo;

    private List<AptitudeResponse> aptitudes;

    private UbicationDto ubicacion;

    private List<LanguageResponse> lenguajes;

    private List<EducationResponse> educaciones;

    private List<RedesContactoResponse> redContactos;

}
