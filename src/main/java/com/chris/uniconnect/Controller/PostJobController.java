package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.VacanteStatus;
import com.chris.uniconnect.Enum.WorkModality;
import com.chris.uniconnect.Enum.WorkingDay;
import com.chris.uniconnect.Model.Dto.PostJobDto;
import com.chris.uniconnect.Model.Dto.PostJobRequest;
import com.chris.uniconnect.Service.IPostJobService;
import com.chris.uniconnect.payload.MensajeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vacantes", description = "Vacantes de las empresas con convenio. Ciclo: BORRADOR -> PUBLICADA <-> PAUSADA -> CERRADA; " +
        "VENCIDA no se guarda, se calcula cuando una PUBLICADA pasa su fecha de cierre. " +
        "El RECRUITER crea y edita las suyas; el COMPANY_ADMIN ve todas las de su empresa y solo modera el estado. " +
        "La busqueda publica no requiere login y solo muestra vacantes publicadas, no vencidas y de convenios vigentes.")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class PostJobController {

    private final IPostJobService postJobService;

    @Operation(summary = "Crear una vacante (borrador)",
            description = "Solo RECRUITER. Nace en BORRADOR, ligada al reclutador y a su empresa. Solo el titulo es obligatorio para el borrador.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vacante creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, salario min > max, o carrera/tecnologia inexistente"),
            @ApiResponse(responseCode = "403", description = "No es RECRUITER")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/recruiter/me/jobs")
    public ResponseEntity<?> createJob(@Valid @RequestBody PostJobRequest request, Authentication authentication) {
        PostJobDto postJob = postJobService.createJob(authentication.getName(), request);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Vacante creada con exito")
                .object(postJob).build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Mis vacantes", description = "Solo RECRUITER. Filtro opcional por estado (incluye VENCIDA).")
    @ApiResponse(responseCode = "200", description = "Lista de vacantes (puede ser un array vacio)")
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/recruiter/me/jobs")
    public ResponseEntity<List<PostJobDto>> getMyJobs(@Parameter(description = "Estado (opcional)") @RequestParam(required = false) VacanteStatus estado,
                                                      Authentication authentication) {
        return ResponseEntity.ok(postJobService.getMyJobs(authentication.getName(), estado));
    }

    @Operation(summary = "Detalle de una vacante mia", description = "Solo RECRUITER. Una vacante de otro reclutador responde 404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vacante encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe la vacante entre las tuyas")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/recruiter/me/jobs/{id}")
    public ResponseEntity<PostJobDto> getMyJob(@Parameter(description = "Id de la vacante") @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(postJobService.getMyJob(authentication.getName(), id));
    }

    @Operation(summary = "Editar una vacante mia",
            description = "Solo RECRUITER. Reemplaza todos los campos (los opcionales que no se manden quedan vacios). " +
                    "No se puede editar una CERRADA, y una PUBLICADA/PAUSADA no puede quedar sin descripcion, jornada, modalidad o tipo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vacante actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, vacante cerrada o vacante publicada incompleta"),
            @ApiResponse(responseCode = "404", description = "No existe la vacante entre las tuyas")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/recruiter/me/jobs/{id}")
    public ResponseEntity<?> updateMyJob(@Parameter(description = "Id de la vacante") @PathVariable Long id,
                                         @Valid @RequestBody PostJobRequest request, Authentication authentication) {
        PostJobDto postJob = postJobService.updateMyJob(authentication.getName(), id, request);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Vacante actualizada con exito")
                .object(postJob).build());
    }

    @Operation(summary = "Cambiar el estado de una vacante mia",
            description = "Solo RECRUITER. Destinos: PUBLICADA, PAUSADA, CERRADA. Publicar exige descripcion, jornada, modalidad y tipo, " +
                    "convenio vigente y fecha de cierre no vencida. CERRADA es definitivo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Transicion no valida, vacante incompleta o convenio no vigente"),
            @ApiResponse(responseCode = "404", description = "No existe la vacante entre las tuyas")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @PatchMapping("/recruiter/me/jobs/{id}/status")
    public ResponseEntity<?> changeMyJobStatus(@Parameter(description = "Id de la vacante") @PathVariable Long id,
                                               @Parameter(description = "PUBLICADA, PAUSADA o CERRADA") @RequestParam VacanteStatus estado,
                                               Authentication authentication) {
        PostJobDto postJob = postJobService.changeMyJobStatus(authentication.getName(), id, estado);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Estado de la vacante actualizado a " + estado)
                .object(postJob).build());
    }

    @Operation(summary = "Eliminar una vacante mia", description = "Solo RECRUITER y solo en BORRADOR; las publicadas se cierran.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vacante eliminada"),
            @ApiResponse(responseCode = "400", description = "La vacante no es un borrador"),
            @ApiResponse(responseCode = "404", description = "No existe la vacante entre las tuyas")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/recruiter/me/jobs/{id}")
    public ResponseEntity<?> deleteMyJob(@Parameter(description = "Id de la vacante") @PathVariable Long id, Authentication authentication) {
        postJobService.deleteMyJob(authentication.getName(), id);
        return ResponseEntity.ok(MensajeResponse.builder().mensaje("Vacante eliminada con exito").build());
    }

    @Operation(summary = "Vacantes de mi empresa", description = "Solo COMPANY_ADMIN. Todas las de su convenio, filtros opcionales por estado y reclutador.")
    @ApiResponse(responseCode = "200", description = "Lista de vacantes (puede ser un array vacio)")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @GetMapping("/company-admin/me/jobs")
    public ResponseEntity<List<PostJobDto>> getCompanyJobs(@Parameter(description = "Estado (opcional)") @RequestParam(required = false) VacanteStatus estado,
                                                           @Parameter(description = "Id del reclutador (opcional)") @RequestParam(required = false) Integer idReclutador,
                                                           Authentication authentication) {
        return ResponseEntity.ok(postJobService.getCompanyJobs(authentication.getName(), estado, idReclutador));
    }

    @Operation(summary = "Moderar el estado de una vacante de mi empresa",
            description = "Solo COMPANY_ADMIN. Puede pausar, reanudar o cerrar; no puede publicar borradores ni editar el contenido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Transicion no valida, borrador o convenio no vigente"),
            @ApiResponse(responseCode = "404", description = "No existe la vacante en tu empresa")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @PatchMapping("/company-admin/me/jobs/{id}/status")
    public ResponseEntity<?> changeCompanyJobStatus(@Parameter(description = "Id de la vacante") @PathVariable Long id,
                                                    @Parameter(description = "PUBLICADA, PAUSADA o CERRADA") @RequestParam VacanteStatus estado,
                                                    Authentication authentication) {
        PostJobDto postJob = postJobService.changeCompanyJobStatus(authentication.getName(), id, estado);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Estado de la vacante actualizado a " + estado)
                .object(postJob).build());
    }

    @Operation(summary = "Buscar vacantes (publico)", description = "No requiere login. Paginado, de la mas reciente a la mas antigua. Todos los filtros son opcionales.")
    @ApiResponse(responseCode = "200", description = "Pagina de vacantes")
    @GetMapping("/jobs")
    public ResponseEntity<Page<PostJobDto>> searchJobs(@Parameter(description = "Texto en titulo o descripcion") @RequestParam(required = false) String q,
                                                       @RequestParam(required = false) TypeApplication tipo,
                                                       @RequestParam(required = false) WorkingDay jornada,
                                                       @RequestParam(required = false) WorkModality modalidad,
                                                       @RequestParam(required = false) Integer idCarrera,
                                                       @RequestParam(required = false) Integer idTecnologia,
                                                       @RequestParam(required = false) Long idCorporacion,
                                                       @Parameter(description = "Numero de pagina, desde 0") @RequestParam(defaultValue = "0") int page,
                                                       @Parameter(description = "Tamano de pagina") @RequestParam(defaultValue = "20") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publicationDate", "idPostJob"));
        return ResponseEntity.ok(postJobService.searchJobs(q, tipo, jornada, modalidad, idCarrera, idTecnologia, idCorporacion, pageable));
    }

    @Operation(summary = "Detalle de una vacante (publico)", description = "No requiere login. Si no esta visible (borrador, pausada, cerrada, vencida o convenio no vigente) responde 404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vacante encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe o no esta visible")
    })
    @GetMapping("/jobs/{id}")
    public ResponseEntity<PostJobDto> getPublicJob(@Parameter(description = "Id de la vacante") @PathVariable Long id) {
        return ResponseEntity.ok(postJobService.getPublicJob(id));
    }

    @Operation(summary = "Vacantes de una empresa (publico)", description = "No requiere login. Atajo de GET /jobs?idCorporacion= para la landing.")
    @ApiResponse(responseCode = "200", description = "Pagina de vacantes")
    @GetMapping("/companies/{idCorporacion}/jobs")
    public ResponseEntity<Page<PostJobDto>> getCompanyPublicJobs(@Parameter(description = "Id del convenio") @PathVariable Long idCorporacion,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "20") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publicationDate", "idPostJob"));
        return ResponseEntity.ok(postJobService.searchJobs(null, null, null, null, null, null, idCorporacion, pageable));
    }
}
