package com.chris.uniconnect.Model.Dto;

import java.util.List;

//filtros de GET /students (todos opcionales). byRecommendations: orden "recomendaciones" (va en la consulta, no en el Sort)
public record StudentSearchRequest(String q, Integer idCareer, Integer semester, Integer idUbication,
                                   List<Integer> idTechnologies, Boolean withProjects, Boolean recommended,
                                   boolean byRecommendations) {
}
