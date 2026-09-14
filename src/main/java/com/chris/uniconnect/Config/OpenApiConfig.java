package com.chris.uniconnect.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "UniConnect API",
                version = "v1",
                description = "API del Centro Universitario UAEMEX Tianguistenco: perfiles de estudiantes, profesores y reclutadores, proyectos, publicaciones y notificaciones."
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT obtenido en POST /auth/log-in. Pegalo aca sin el prefijo 'Bearer '."
)
@Configuration
public class OpenApiConfig {
}
