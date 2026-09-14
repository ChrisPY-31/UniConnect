package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.ChangePasswordDto;
import com.chris.uniconnect.Model.Dto.ResetPasswordDto;
import com.chris.uniconnect.Service.Impl.UserDetailsServiceImpl;
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

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Tag(name = "Usuarios", description = "Administracion de cuentas y configuracion de contrasena")
public class UserConfigController {

    private final UserDetailsServiceImpl userDetailsService;

    @Operation(summary = "Listar cuentas", description = "Solo ADMIN. No incluye el hash de la contrasena.")
    @ApiResponse(responseCode = "200", description = "Listado de cuentas")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(userDetailsService.getUsers(), HttpStatus.OK);
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
