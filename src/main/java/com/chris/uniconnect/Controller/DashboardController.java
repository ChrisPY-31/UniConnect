package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Service.Impl.DashboardServiceImpl;
import com.chris.uniconnect.payload.MensajeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Panel administrativo", description = "Metricas y bienvenida para el rol ADMIN.")
@RestController
@RequestMapping("/api/v1")
public class DashboardController {

    @Autowired
    private DashboardServiceImpl dashboardService;

    @Operation(summary = "Mensaje de bienvenida del panel de ADMIN")
    @ApiResponse(responseCode = "200", description = "Mensaje de bienvenida")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manager")
    public ResponseEntity<?> dashboardManager() {
        return ResponseEntity.ok(MensajeResponse.builder().mensaje("bienvenido administraodor").build());
    }

    @Operation(summary = "Metricas generales del sistema", description = "Conteos y resumen para el panel de ADMIN.")
    @ApiResponse(responseCode = "200", description = "Resumen de metricas")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/Usuarios")
    public ResponseEntity<?> dashboardUsuarios() {
        try {
            return ResponseEntity.ok(MensajeResponse.builder().mensaje("Respuesta correctamente").object(dashboardService.dashBoardResponse()).build());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
