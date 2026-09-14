package com.chris.uniconnect.Controller;


import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.RegisterRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthCreateUserRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthLoginRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthResponse;
import com.chris.uniconnect.Service.Impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticacion", description = "Registro e inicio de sesion. No requieren token.")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Operation(
            summary = "Crear cuenta",
            description = "Crea el usuario, le asigna los roles pedidos y crea el perfil (Student/Teacher/Recruiter) asociado. Envia un correo de bienvenida con la contrasena en texto plano."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada, devuelve el token de acceso"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos, correo ya registrado o rol inexistente")
    })
    @SecurityRequirements
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        AuthCreateUserRequest authCreateUserRequest = registerRequest.getUser();
        PersonDto person = registerRequest.getPerson();
        return new ResponseEntity<>(this.userDetailsService.createUser(authCreateUserRequest , person), HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar sesion", description = "Devuelve el JWT a usar como Bearer token en el resto de la API.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login exitoso, devuelve el token"),
            @ApiResponse(responseCode = "401", description = "Usuario, contrasena, cuenta bloqueada o deshabilitada")
    })
    @SecurityRequirements
    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {

        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }


}
