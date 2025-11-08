package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DashboardController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/manager")
    private ResponseEntity<?> dashboardManager(){
        return ResponseEntity.ok(MensajeResponse.builder().mensaje("bienvenido administraodor").build());
    };
}
