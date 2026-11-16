package com.chris.uniconnect.Model.Dto.Response;

import com.chris.uniconnect.Enum.VacanteStatus;

//version corta de una vacante para los selectores (estado = estado efectivo)
public record JobOptionResponse(Long idVacante, String titulo, VacanteStatus estado) {
}
