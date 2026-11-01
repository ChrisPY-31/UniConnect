package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.CreateRecruiterRequest;
import com.chris.uniconnect.Model.Dto.RecruiterAccountDto;
import com.chris.uniconnect.Service.IRecruiterAccountService;
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

@Tag(name = "Cuentas de reclutador", description = "El COMPANY_ADMIN crea y administra los reclutadores de su propio convenio (se resuelve por JWT). " +
        "No hay borrado: la baja es desactivacion, para conservar vacantes, postulaciones y chats. " +
        "El ADMIN solo puede consultarlos por convenio y activarlos/desactivarlos como control. " +
        "Si el convenio se desactiva, sus reclutadores no pueden iniciar sesion.")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RecruiterAccountController {

    private final IRecruiterAccountService recruiterAccountService;

    @Operation(summary = "Crear un reclutador de mi empresa",
            description = "Solo COMPANY_ADMIN. El reclutador queda ligado al convenio del usuario autenticado. " +
                    "La contrasena la genera el sistema y solo se envia por correo; si el correo no se puede enviar, la cuenta no se crea.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reclutador creado y correo enviado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, convenio desactivado, username/correo en uso o fallo el envio del correo"),
            @ApiResponse(responseCode = "403", description = "No es COMPANY_ADMIN")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @PostMapping("/company-admin/me/recruiters")
    public ResponseEntity<?> createRecruiter(@Valid @RequestBody CreateRecruiterRequest request, Authentication authentication) {
        RecruiterAccountDto recruiter = recruiterAccountService.createRecruiter(authentication.getName(), request);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Reclutador creado con exito, la contrasena se envio por correo")
                .object(recruiter).build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar los reclutadores de mi empresa", description = "Solo COMPANY_ADMIN. Filtro opcional por estado de la cuenta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reclutadores (puede ser un array vacio)"),
            @ApiResponse(responseCode = "403", description = "No es COMPANY_ADMIN")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @GetMapping("/company-admin/me/recruiters")
    public ResponseEntity<List<RecruiterAccountDto>> getMyRecruiters(
            @Parameter(description = "true = solo activos, false = solo desactivados (opcional)") @RequestParam(required = false) Boolean activo,
            Authentication authentication) {
        return ResponseEntity.ok(recruiterAccountService.getMyRecruiters(authentication.getName(), activo));
    }

    @Operation(summary = "Detalle de un reclutador de mi empresa", description = "Solo COMPANY_ADMIN. Un reclutador de otro convenio responde 404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclutador encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe el reclutador en tu convenio"),
            @ApiResponse(responseCode = "403", description = "No es COMPANY_ADMIN")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @GetMapping("/company-admin/me/recruiters/{id}")
    public ResponseEntity<RecruiterAccountDto> getMyRecruiter(@Parameter(description = "Id del reclutador") @PathVariable Integer id,
                                                              Authentication authentication) {
        return ResponseEntity.ok(recruiterAccountService.getMyRecruiter(authentication.getName(), id));
    }

    @Operation(summary = "Activar o desactivar un reclutador de mi empresa",
            description = "Solo COMPANY_ADMIN. No se puede activar si el convenio esta desactivado. " +
                    "Un token ya emitido sigue siendo valido hasta que expira (30 min).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "El convenio esta desactivado"),
            @ApiResponse(responseCode = "404", description = "No existe el reclutador en tu convenio"),
            @ApiResponse(responseCode = "403", description = "No es COMPANY_ADMIN")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @PatchMapping("/company-admin/me/recruiters/{id}/status")
    public ResponseEntity<?> changeMyRecruiterStatus(@Parameter(description = "Id del reclutador") @PathVariable Integer id,
                                                     @Parameter(description = "true = activar, false = desactivar") @RequestParam boolean activo,
                                                     Authentication authentication) {
        RecruiterAccountDto recruiter = recruiterAccountService.changeMyRecruiterStatus(authentication.getName(), id, activo);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje(activo ? "Reclutador activado con exito" : "Reclutador desactivado con exito")
                .object(recruiter).build());
    }

    @Operation(summary = "Restablecer la contrasena de un reclutador de mi empresa",
            description = "Solo COMPANY_ADMIN. Genera una contrasena nueva y la envia al correo del reclutador; si el correo falla, se conserva la anterior.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrasena restablecida y correo enviado"),
            @ApiResponse(responseCode = "400", description = "La cuenta esta desactivada o fallo el envio del correo"),
            @ApiResponse(responseCode = "404", description = "No existe el reclutador en tu convenio"),
            @ApiResponse(responseCode = "403", description = "No es COMPANY_ADMIN")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @PostMapping("/company-admin/me/recruiters/{id}/reset-password")
    public ResponseEntity<?> resetMyRecruiterPassword(@Parameter(description = "Id del reclutador") @PathVariable Integer id,
                                                      Authentication authentication) {
        recruiterAccountService.resetMyRecruiterPassword(authentication.getName(), id);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Contrasena restablecida con exito, se envio al correo del reclutador")
                .build());
    }

    @Operation(summary = "Listar los reclutadores de un convenio", description = "Solo ADMIN, solo lectura.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reclutadores (puede ser un array vacio)"),
            @ApiResponse(responseCode = "404", description = "No existe el convenio"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/partnerships/{id}/recruiters")
    public ResponseEntity<List<RecruiterAccountDto>> getRecruitersByPartnership(@Parameter(description = "Id del convenio") @PathVariable Long id) {
        return ResponseEntity.ok(recruiterAccountService.getRecruitersByPartnership(id));
    }

    @Operation(summary = "Activar o desactivar un reclutador (control del ADMIN)",
            description = "Solo ADMIN. No se puede activar si el convenio esta desactivado. " +
                    "Nota: el COMPANY_ADMIN de la empresa tambien puede volver a activarlo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "El convenio esta desactivado"),
            @ApiResponse(responseCode = "404", description = "No existe el reclutador"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/recruiters/{id}/status")
    public ResponseEntity<?> changeRecruiterStatus(@Parameter(description = "Id del reclutador") @PathVariable Integer id,
                                                   @Parameter(description = "true = activar, false = desactivar") @RequestParam boolean activo) {
        RecruiterAccountDto recruiter = recruiterAccountService.changeRecruiterStatus(id, activo);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje(activo ? "Reclutador activado con exito" : "Reclutador desactivado con exito")
                .object(recruiter).build());
    }
}
