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

@Tag(name = "Notificaciones", description = """
        Notificaciones de menciones en proyectos y recomendaciones. Cada notificacion se guarda en BD \
        (consultable con los endpoints REST de abajo) y ademas se envia al instante por WebSocket si el usuario esta conectado.

        **Cuando se generan**
        - `RECOMMENDATION`: un TEACHER crea una recomendacion (`POST /api/v1/recomendation`). La recibe el estudiante recomendado; `referenceId` = id del profesor.
        - `PROJECT_MENTION`: un estudiante crea un proyecto mencionando a otros (`POST /api/v1/project`) o agrega menciones nuevas al editarlo (`PUT`). \
        Solo se notifica a los mencionados nuevos, no a los que ya estaban. `referenceId` = id del proyecto.

        **Tiempo real (STOMP sobre SockJS)**
        1. Conectar con SockJS a `http://<host>:8080/ws` (no es WebSocket puro: el cliente debe usar SockJS, p. ej. `sockjs-client` + `@stomp/stompjs`).
        2. En el frame `CONNECT` mandar el header nativo `Authorization: Bearer <jwt>` (el mismo token de `POST /auth/log-in`). \
        Sin token o con token invalido el servidor rechaza la conexion. `/ws` no pasa por el filtro JWT de HTTP; se valida en el CONNECT.
        3. Suscribirse a `/user/queue/notifications`. Cada usuario recibe solo las suyas (el servidor resuelve el destino por el username del JWT).
        4. Cada mensaje es un JSON con la misma forma que la respuesta REST:
        `{ "id": 7, "type": "PROJECT_MENTION", "message": "Fuiste mencionado en el proyecto X", "referenceId": 3, "read": false, "createdAt": "2026-10-01T11:36:58.123" }`

        Si el usuario no esta conectado no se pierde nada: la notificacion queda en BD y se obtiene con `GET /notifications/{personId}` al entrar. \
        Flujo sugerido en el front: al iniciar sesion cargar el historial por REST y luego quedarse escuchando el WebSocket.

        **Limitaciones actuales**
        - CORS del WebSocket solo permite el origen `http://localhost:5173`; otro origen (p. ej. el front en prod) no podra conectarse.
        - PENDIENTE: `personId` y el `id` de marcar como leida no se validan contra el usuario autenticado.
        """)
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

    @Operation(
            summary = "Marcar una notificacion como leida",
            description = "ADVERTENCIA: no se valida que la notificacion sea del usuario autenticado; cualquiera con estos roles puede marcar como leida la de otra persona. Pendiente de cerrar."
    )
    @ApiResponse(responseCode = "200", description = "Notificacion actualizada")
    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<?> markAsRead(@Parameter(description = "Id de la notificacion") @PathVariable Integer id) {
        return new ResponseEntity<>(notificationService.markAsRead(id), HttpStatus.OK);
    }
}
