package com.chris.uniconnect.Model.Dto.Response;

import java.util.Map;

//numeros de las pestanas de estado: porEstado usa el estado efectivo (una PUBLICADA vencida cuenta como VENCIDA)
public record JobStatsResponse(long total, Map<String, Long> porEstado) {
}
