package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.ChangePasswordDto;
import com.chris.uniconnect.Model.Dto.ResetPasswordDto;
import com.chris.uniconnect.Model.Dto.Response.UserSummaryResponse;
import com.chris.uniconnect.Model.Dto.Response.UserStatsResponse;
import com.chris.uniconnect.Enum.Roles;
import com.chris.uniconnect.Enum.UserStatus;
import com.chris.uniconnect.Service.Impl.UserDetailsServiceImpl;
import com.chris.uniconnect.util.PageRequests;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Tag(name = "Usuarios", description = "Administracion de cuentas y configuracion de contrasena")
public class UserConfigController {

    private final UserDetailsServiceImpl userDetailsService;

    @Operation(summary = "Listar cuentas (paginado y con filtros)",
            description = "Solo ADMIN. No incluye el hash de la contrasena. Ordenado por id ascendente. "
                    + "Todos los filtros son opcionales y totalElements es el de los resultados ya filtrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagina de cuentas"),
            @ApiResponse(responseCode = "400", description = "page < 0, size fuera de 1..50 o rol/estado invalido")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserSummaryResponse>> getUsers(@Parameter(description = "Texto en username o correo (sin importar mayusculas)") @RequestParam(required = false) String q,
                                                              @Parameter(description = "Rol") @RequestParam(required = false) Roles rol,
                                                              @Parameter(description = "ACTIVA, BLOQUEADA o DESHABILITADA") @RequestParam(required = false) UserStatus estado,
                                                              @Parameter(description = "Numero de pagina, desde 0") @RequestParam(defaultValue = "0") int page,
                                                              @Parameter(description = "Tamano de pagina (1..50)") @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(userDetailsService.getUsers(q, rol, estado, PageRequests.of(page, size, Sort.by(Sort.Direction.ASC, "id"))));
    }

    @Operation(summary = "Resumen de cuentas", description = "Solo ADMIN. Total de cuentas, cuantas hay por rol y cuantas estan bloqueadas (habilitadas pero con accountNonLocked = false).")
    @ApiResponse(responseCode = "200", description = "Resumen")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/summary")
    public ResponseEntity<UserStatsResponse> getUsersSummary() {
        return ResponseEntity.ok(userDetailsService.getUserStats());
    }

    @Operation(summary = "Bloquear/desbloquear una cuenta", description = "Solo ADMIN. Es un toggle: invierte el estado actual. El id es el de la persona (Student/Teacher/Recruiter), no el de UserEntity.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nuevo estado de la cuenta"),
            @ApiResponse(responseCode = "404", description = "No existe una cuenta para esa persona")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/blocked")
    public ResponseEntity<?> studentBlocked(@Parameter(description = "Id de la persona (no de la cuenta)") @PathVariable Integer id) {
        return new ResponseEntity<>(userDetailsService.userBlocked(id), HttpStatus.OK);
    }

    @Operation(summary = "Cambiar mi contrasena", description = "Self-service: requiere la contrasena actual. No necesita username en el path, se toma del JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrasena actualizada"),
            @ApiResponse(responseCode = "400", description = "La contrasena actual no coincide, o la nueva no cumple el minimo de 8 caracteres")
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, Authentication authentication) {
        return new ResponseEntity<>(userDetailsService.changePassword(authentication.getName(),
                changePasswordDto.getCurrentPassword(), changePasswordDto.getNewPassword()), HttpStatus.OK);
    }

    @Operation(
            summary = "Restablecer la contrasena de un usuario",
            description = "Solo ADMIN. No pide la contrasena actual. Le envia la nueva contrasena por correo al usuario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrasena restablecida"),
            @ApiResponse(responseCode = "400", description = "La nueva contrasena no cumple el minimo de 8 caracteres"),
            @ApiResponse(responseCode = "401", description = "No existe un usuario con ese username")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}/password/reset")
    public ResponseEntity<?> resetPassword(@Parameter(description = "Username de la cuenta") @PathVariable String username,
                                            @Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        return new ResponseEntity<>(userDetailsService.resetPassword(username, resetPasswordDto.getNewPassword()), HttpStatus.OK);
    }

}
