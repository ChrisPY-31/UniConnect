package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.CorporatePartnershipsUAEMexDto;
import com.chris.uniconnect.Service.ICorporatePartnershipsService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Convenios", description = "Convenios de empresas con la UAEMex. Solo ADMIN. No hay borrado fisico: se desactivan para no perder el historial de reclutadores y vacantes.")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CorporatePartnershipsController {

    private final ICorporatePartnershipsService partnershipsService;

    @Operation(summary = "Crear convenio", description = "Rechaza nombres de empresa duplicados (case-insensitive). fechaFinal es opcional (null = sin fecha de termino).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Convenio creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, nombre duplicado o fechaFinal anterior a fechaInicio"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PostMapping("/partnerships")
    public ResponseEntity<?> createPartnership(@Valid @RequestBody CorporatePartnershipsUAEMexDto partnership) {
        CorporatePartnershipsUAEMexDto savedPartnership = partnershipsService.createPartnership(partnership);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Convenio creado con exito")
                .object(savedPartnership).build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar convenios", description = "Sin filtro devuelve todos. vigente=true: activos, ya iniciados y sin vencer. vigente=false: el resto (inactivos, vencidos o que aun no inician).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de convenios (puede ser un array vacio)"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @GetMapping("/partnerships")
    public ResponseEntity<List<CorporatePartnershipsUAEMexDto>> getPartnerships(
            @Parameter(description = "Filtrar por vigencia (opcional)") @RequestParam(required = false) Boolean vigente) {
        return ResponseEntity.ok(partnershipsService.getPartnerships(vigente));
    }

    @Operation(summary = "Detalle de un convenio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Convenio encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un convenio con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @GetMapping("/partnerships/{id}")
    public ResponseEntity<CorporatePartnershipsUAEMexDto> getPartnershipById(
            @Parameter(description = "Id del convenio") @PathVariable Long id) {
        return ResponseEntity.ok(partnershipsService.getPartnershipById(id));
    }

    @Operation(summary = "Editar convenio", description = "Actualiza nombre, categoria, tipo de acuerdo y fechas. No cambia el estado activo/inactivo (usa PATCH /partnerships/{id}/status).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Convenio actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, nombre duplicado o fechaFinal anterior a fechaInicio"),
            @ApiResponse(responseCode = "404", description = "No existe un convenio con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PutMapping("/partnerships/{id}")
    public ResponseEntity<?> updatePartnership(@Parameter(description = "Id del convenio") @PathVariable Long id,
                                               @Valid @RequestBody CorporatePartnershipsUAEMexDto partnership) {
        CorporatePartnershipsUAEMexDto updatedPartnership = partnershipsService.updatePartnership(id, partnership);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Convenio actualizado con exito")
                .object(updatedPartnership).build());
    }

    @Operation(summary = "Activar o desactivar convenio", description = "Baja logica. Al reactivar no se validan fechas: si el convenio ya vencio la respuesta trae vigente=false y hay que extender fechaFinal con el PUT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "404", description = "No existe un convenio con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PatchMapping("/partnerships/{id}/status")
    public ResponseEntity<?> changeStatus(@Parameter(description = "Id del convenio") @PathVariable Long id,
                                          @Parameter(description = "true = activar, false = desactivar") @RequestParam boolean activo) {
        CorporatePartnershipsUAEMexDto partnership = partnershipsService.changeStatus(id, activo);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje(activo ? "Convenio activado con exito" : "Convenio desactivado con exito")
                .object(partnership).build());
    }
}
