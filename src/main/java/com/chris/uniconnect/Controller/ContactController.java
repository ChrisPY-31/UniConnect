package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Service.IContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Contactos", description = "Catalogo fijo de tipos de red de contacto (LinkedIn, email, telefono, web) usado por PersonContact.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class ContactController {

    private final IContactService contactService;

    @Operation(summary = "Listar tipos de contacto disponibles")
    @ApiResponse(responseCode = "200", description = "Catalogo completo")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/contact")
    public ResponseEntity<?> getContacts() {
        return ResponseEntity.ok(contactService.getContacts());
    }
}
