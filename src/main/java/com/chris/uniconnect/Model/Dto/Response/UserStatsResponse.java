package com.chris.uniconnect.Model.Dto.Response;

import java.util.Map;

//porRol cuenta usuarios distintos por rol; si una cuenta tuviera varios roles, la suma de porRol puede ser mayor que total
public record UserStatsResponse(long total, Map<String, Long> porRol, long bloqueadas) {
}
