package com.chris.uniconnect.Model.Dto.Response;

import java.util.Map;

//numeros de las pestanas de estado de las postulaciones
public record ApplicationStatsResponse(long total, Map<String, Long> porEstado) {
}
