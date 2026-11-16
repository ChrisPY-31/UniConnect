package com.chris.uniconnect.Model.Dto.Response;

import java.util.List;

//GET /students/summary: porCarrera incluye todas las carreras del catalogo (aunque tengan 0 alumnos)
public record StudentStatsResponse(long total, List<CareerCount> porCarrera, long sinCarrera) {

    public record CareerCount(Integer idCarrera, String carrera, long cantidad) {
    }
}
