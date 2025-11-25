package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Service.Impl.DashboardServiceImpl;
import com.chris.uniconnect.payload.MensajeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DashboardController {

    @Autowired
    private DashboardServiceImpl dashboardService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manager")
    public ResponseEntity<?> dashboardManager() {
        return ResponseEntity.ok(MensajeResponse.builder().mensaje("bienvenido administraodor").build());
    }

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
