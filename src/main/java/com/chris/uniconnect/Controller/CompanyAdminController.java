package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.CompanyAdminDto;
import com.chris.uniconnect.Model.Dto.CompanyAdminProfileRequest;
import com.chris.uniconnect.Model.Dto.CreateCompanyAdminRequest;
import com.chris.uniconnect.Service.ICompanyAdminService;
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

@Tag(name = "Cuentas de empresa", description = "Cuenta COMPANY_ADMIN de cada convenio: el ADMIN la crea (una sola activa por convenio) y la empresa gestiona su perfil. " +
        "Si el convenio se desactiva, la cuenta no puede iniciar sesion.")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CompanyAdminController {

    private final ICompanyAdminService companyAdminService;

    @Operation(summary = "Crear la cuenta de empresa de un convenio",
            description = "Solo ADMIN. La contrasena la genera el sistema y solo se envia por correo; si el correo no se puede enviar, la cuenta no se crea.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada y correo enviado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, convenio desactivado, ya hay una cuenta activa, username/correo en uso o fallo el envio del correo"),
            @ApiResponse(responseCode = "404", description = "No existe el convenio"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/partnerships/{id}/company-admin")
    public ResponseEntity<?> createCompanyAdmin(@Parameter(description = "Id del convenio") @PathVariable Long id,
                                                @Valid @RequestBody CreateCompanyAdminRequest request) {
        CompanyAdminDto companyAdmin = companyAdminService.createCompanyAdmin(id, request);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Cuenta de empresa creada con exito, la contrasena se envio por correo")
                .object(companyAdmin).build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar cuentas de empresa", description = "Solo ADMIN. Filtro opcional por convenio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cuentas (puede ser un array vacio)"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/company-admins")
    public ResponseEntity<List<CompanyAdminDto>> getCompanyAdmins(
            @Parameter(description = "Id del convenio (opcional)") @RequestParam(required = false) Long idCorporacion) {
        return ResponseEntity.ok(companyAdminService.getCompanyAdmins(idCorporacion));
    }

    @Operation(summary = "Detalle de una cuenta de empresa", description = "Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe la cuenta"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/company-admins/{id}")
    public ResponseEntity<CompanyAdminDto> getCompanyAdminById(@Parameter(description = "Id de la persona") @PathVariable Integer id) {
        return ResponseEntity.ok(companyAdminService.getCompanyAdminById(id));
    }

    @Operation(summary = "Activar o desactivar una cuenta de empresa",
            description = "Solo ADMIN. No se puede reactivar si el convenio ya tiene otra cuenta activa. " +
                    "Un token ya emitido sigue siendo valido hasta que expira (30 min).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "El convenio ya tiene otra cuenta activa"),
            @ApiResponse(responseCode = "404", description = "No existe la cuenta"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/company-admins/{id}/status")
    public ResponseEntity<?> changeStatus(@Parameter(description = "Id de la persona") @PathVariable Integer id,
                                          @Parameter(description = "true = activar, false = desactivar") @RequestParam boolean activo) {
        CompanyAdminDto companyAdmin = companyAdminService.changeStatus(id, activo);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje(activo ? "Cuenta de empresa activada con exito" : "Cuenta de empresa desactivada con exito")
                .object(companyAdmin).build());
    }

    @Operation(summary = "Mi perfil de empresa", description = "Solo COMPANY_ADMIN. Incluye los datos del convenio. El dueno se resuelve por JWT.")
    @ApiResponse(responseCode = "200", description = "Perfil del usuario autenticado")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @GetMapping("/company-admin/me")
    public ResponseEntity<CompanyAdminDto> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(companyAdminService.getMyProfile(authentication.getName()));
    }

    @Operation(summary = "Editar mi perfil de empresa",
            description = "Solo COMPANY_ADMIN. Edita nombre, apellido, puesto y descripcion. El convenio no se puede cambiar. " +
                    "Para la contrasena usa PUT /api/v1/users/password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "400", description = "Nombre o apellido vacios")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @PutMapping("/company-admin/me")
    public ResponseEntity<?> updateMyProfile(@Valid @RequestBody CompanyAdminProfileRequest request, Authentication authentication) {
        CompanyAdminDto companyAdmin = companyAdminService.updateMyProfile(authentication.getName(), request);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Perfil actualizado con exito")
                .object(companyAdmin).build());
    }
}
