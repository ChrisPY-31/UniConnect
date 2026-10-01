package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Service.ISkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Habilidades", description = "Catalogo combinado de tecnologias y aptitudes para la seccion Habilidades del perfil.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class SkillController {

    private final ISkillService skillService;

    @Operation(
            summary = "Listar catalogo de habilidades",
            description = "Tecnologias y aptitudes en una sola lista. Los ids se repiten entre tipos, asi que el front debe usar el par (id, tipo)."
    )
    @ApiResponse(responseCode = "200", description = "Catalogo completo, tecnologias primero")
    @GetMapping("/skills/catalog")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCatalog() {
        return ResponseEntity.ok(skillService.getCatalog());
    }
}
