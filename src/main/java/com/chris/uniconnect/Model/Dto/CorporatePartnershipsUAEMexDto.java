package com.chris.uniconnect.Model.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorporatePartnershipsUAEMexDto {

    private Long idCorporacion;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;

    private String categoria;

    private String tipoAcuerdo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFinal;

    //solo lectura: el estado solo cambia con PATCH /partnerships/{id}/status
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean activo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean vigente;

}
