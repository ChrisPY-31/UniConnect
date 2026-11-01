package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Model.Dto.JobApplicationDto;
import com.chris.uniconnect.Model.Dto.JobApplicationRequest;
import com.chris.uniconnect.Service.IJobApplicationService;
import com.chris.uniconnect.payload.MensajeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Postulaciones", description = "Postulaciones de estudiantes a vacantes. Estados: ENVIADA -> EN_REVISION -> ENTREVISTA -> ACEPTADA; " +
        "RECHAZADA (reclutador) y RETIRADA (estudiante) desde cualquier estado abierto. ACEPTADA, RECHAZADA y RETIRADA son finales. " +
        "Una postulacion por estudiante y vacante. Al cerrar la vacante, las abiertas pasan a RECHAZADA. " +
        "Se notifica por WebSocket al reclutador (nueva postulacion) y al estudiante (cambio de estado).")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class JobApplicationController {

    private final IJobApplicationService jobApplicationService;

    @Operation(summary = "Postularme a una vacante",
            description = "Solo STUDENT. La vacante debe estar visible. Se adjunta una copia del CV del perfil (obligatorio, ver PATCH /student/me/resume).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Postulacion enviada"),
            @ApiResponse(responseCode = "400", description = "Sin CV, ya postulado o mensaje demasiado largo"),
            @ApiResponse(responseCode = "404", description = "La vacante no existe o no esta visible"),
            @ApiResponse(responseCode = "403", description = "No es STUDENT")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/jobs/{id}/apply")
    public ResponseEntity<?> apply(@Parameter(description = "Id de la vacante") @PathVariable Long id,
                                   @Valid @RequestBody(required = false) JobApplicationRequest request,
                                   Authentication authentication) {
        JobApplicationDto application = jobApplicationService.apply(authentication.getName(), id, request);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Postulacion enviada con exito")
                .object(application).build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Mis postulaciones", description = "Solo STUDENT. Filtro opcional por estado.")
    @ApiResponse(responseCode = "200", description = "Lista de postulaciones (puede ser un array vacio)")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/me/applications")
    public ResponseEntity<List<JobApplicationDto>> getMyApplications(@Parameter(description = "Estado (opcional)") @RequestParam(required = false) AplicationsStatus estado,
                                                                     Authentication authentication) {
        return ResponseEntity.ok(jobApplicationService.getMyApplications(authentication.getName(), estado));
    }

    @Operation(summary = "Retirar mi postulacion", description = "Solo STUDENT y solo si no esta en un estado final. No se puede volver a postular a la misma vacante.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Postulacion retirada"),
            @ApiResponse(responseCode = "400", description = "La postulacion ya esta en un estado final"),
            @ApiResponse(responseCode = "404", description = "No existe la postulacion entre las tuyas")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/student/me/applications/{id}/withdraw")
    public ResponseEntity<?> withdraw(@Parameter(description = "Id de la postulacion") @PathVariable Long id, Authentication authentication) {
        JobApplicationDto application = jobApplicationService.withdraw(authentication.getName(), id);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Postulacion retirada con exito")
                .object(application).build());
    }

    @Operation(summary = "Postulantes de una vacante mia", description = "Solo RECRUITER. Una vacante de otro reclutador responde 404. El perfil completo del estudiante esta en GET /person/{id}.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de postulaciones (puede ser un array vacio)"),
            @ApiResponse(responseCode = "404", description = "No existe la vacante entre las tuyas")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/recruiter/me/jobs/{id}/applications")
    public ResponseEntity<List<JobApplicationDto>> getApplicationsForMyJob(@Parameter(description = "Id de la vacante") @PathVariable Long id,
                                                                           @Parameter(description = "Estado (opcional)") @RequestParam(required = false) AplicationsStatus estado,
                                                                           Authentication authentication) {
        return ResponseEntity.ok(jobApplicationService.getApplicationsForMyJob(authentication.getName(), id, estado));
    }

    @Operation(summary = "Cambiar el estado de una postulacion",
            description = "Solo RECRUITER dueno de la vacante. Solo avanza (EN_REVISION, ENTREVISTA, ACEPTADA) o se rechaza (RECHAZADA). Notifica al estudiante.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Transicion no valida o la postulacion ya es final"),
            @ApiResponse(responseCode = "404", description = "No existe la postulacion en tus vacantes")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @PatchMapping("/recruiter/me/applications/{id}/status")
    public ResponseEntity<?> changeStatus(@Parameter(description = "Id de la postulacion") @PathVariable Long id,
                                          @Parameter(description = "EN_REVISION, ENTREVISTA, ACEPTADA o RECHAZADA") @RequestParam AplicationsStatus estado,
                                          Authentication authentication) {
        JobApplicationDto application = jobApplicationService.changeStatus(authentication.getName(), id, estado);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Estado de la postulacion actualizado a " + estado)
                .object(application).build());
    }

    @Operation(summary = "Postulaciones de mi empresa", description = "Solo COMPANY_ADMIN, solo lectura. Filtros opcionales por vacante y estado.")
    @ApiResponse(responseCode = "200", description = "Lista de postulaciones (puede ser un array vacio)")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @GetMapping("/company-admin/me/applications")
    public ResponseEntity<List<JobApplicationDto>> getCompanyApplications(@Parameter(description = "Id de la vacante (opcional)") @RequestParam(required = false) Long idVacante,
                                                                          @Parameter(description = "Estado (opcional)") @RequestParam(required = false) AplicationsStatus estado,
                                                                          Authentication authentication) {
        return ResponseEntity.ok(jobApplicationService.getCompanyApplications(authentication.getName(), idVacante, estado));
    }
}
