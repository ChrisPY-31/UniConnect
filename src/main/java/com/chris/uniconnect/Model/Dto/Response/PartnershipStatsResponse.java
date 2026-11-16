package com.chris.uniconnect.Model.Dto.Response;

import java.util.List;

//GET /partnerships/summary: tarjetas del panel y sugerencias de categoria / tipo de acuerdo (valores distintos ya usados)
public record PartnershipStatsResponse(long total, long vigentes, long noVigentes, long vencenPronto,
                                       List<String> categorias, List<String> tiposAcuerdo) {
}
