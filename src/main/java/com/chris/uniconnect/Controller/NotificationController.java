package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.Response.NotificationResponse;
import com.chris.uniconnect.Service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notificaciones", description = "Notificaciones instantaneas de menciones en proyectos y recomendaciones. PENDIENTE: el personId no se valida contra el usuario autenticado.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
public class NotificationController {

    private final INotificationService notificationService;

    @Operation(
            summary = "Listar notificaciones de una persona",
            description = "ADVERTENCIA: cualquier usuario con estos roles puede leer las notificaciones de otra persona pasando su personId. Pendiente de cerrar (deberia tomarse del JWT)."
    )
    @ApiResponse(responseCode = "200", description = "Notificaciones de esa persona (vacio si no tiene)")
    @GetMapping("/notifications/{personId}")
    public ResponseEntity<?> getNotifications(@Parameter(description = "Id de la persona") @PathVariable Integer personId) {
        List<NotificationResponse> notifications = notificationService.getNotifications(personId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Marcar una notificacion como leida")
    @ApiResponse(responseCode = "200", description = "Notificacion actualizada")
    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<?> markAsRead(@Parameter(description = "Id de la notificacion") @PathVariable Integer id) {
        return new ResponseEntity<>(notificationService.markAsRead(id), HttpStatus.OK);
    }
}
