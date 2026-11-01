package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Dto.RecruiterProfileRequest;
import com.chris.uniconnect.Service.IRecruiterServce;
import com.chris.uniconnect.payload.MensajeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reclutadores", description = "Perfil propio del Recruiter, resuelto por JWT. Las cuentas las crea el COMPANY_ADMIN (ver Cuentas de reclutador). " +
        "El perfil publico se consulta en GET /api/v1/person/{id} y la foto en PATCH /api/v1/person/me/image.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterController {

    private final IRecruiterServce recruiterService;

    @Operation(summary = "Mi perfil de reclutador", description = "Solo RECRUITER. Incluye el resumen publico de la empresa.")
    @ApiResponse(responseCode = "200", description = "Perfil del usuario autenticado")
    @GetMapping("/recruiter/me")
    public ResponseEntity<RecruiterDto> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(recruiterService.getMyProfile(authentication.getName()));
    }

    @Operation(summary = "Editar mi perfil de reclutador",
            description = "Solo RECRUITER. Reemplaza nombre, apellido, puesto, descripcion, especialidad, ciudad, ubicacion y fecha de nacimiento " +
                    "(los opcionales que no se manden quedan vacios). El convenio y la cuenta no se pueden cambiar; " +
                    "aptitudes/tecnologias van por /person/aptitudes y /person/skills, la contrasena por PUT /api/v1/users/password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "400", description = "Nombre o apellido vacios, fecha de nacimiento futura o ubicacion inexistente")
    })
    @PutMapping("/recruiter/me")
    public ResponseEntity<?> updateMyProfile(@Valid @RequestBody RecruiterProfileRequest request, Authentication authentication) {
        RecruiterDto recruiter = recruiterService.updateMyProfile(authentication.getName(), request);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Perfil actualizado con exito")
                .object(recruiter).build());
    }
}
