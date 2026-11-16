package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.DeleteNotificationsRequest;
import com.chris.uniconnect.Model.Dto.Response.NotificationResponse;
import com.chris.uniconnect.Service.INotificationService;
import com.chris.uniconnect.payload.MensajeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notificaciones", description = """
        Notificaciones de la plataforma. Cada notificacion se guarda en BD \
        (consultable con los endpoints REST de abajo) y ademas se envia al instante por WebSocket si el usuario esta conectado. \
        Todos los endpoints trabajan sobre las notificaciones del usuario autenticado (se resuelve por JWT).

        **Cuando se generan**
        - `RECOMMENDATION`: un TEACHER crea una recomendacion (`POST /api/v1/recomendation`). La recibe el estudiante recomendado; `referenceId` = id del profesor.
        - `PROJECT_MENTION`: un estudiante crea un proyecto mencionando a otros (`POST /api/v1/project`) o agrega menciones nuevas al editarlo (`PUT`). \
        Solo se notifica a los mencionados nuevos, no a los que ya estaban. `referenceId` = id del proyecto.
        - `JOB_APPLICATION_RECEIVED`: al reclutador cuando un estudiante se postula; `referenceId` = id de la vacante.
        - `JOB_APPLICATION_STATUS`: al estudiante cuando cambia su postulacion; `referenceId` = id de la postulacion.

        **Tiempo real (STOMP sobre SockJS)**
        1. Conectar con SockJS a `http://<host>:8080/ws` (no es WebSocket puro: el cliente debe usar SockJS, p. ej. `sockjs-client` + `@stomp/stompjs`).
        2. En el frame `CONNECT` mandar el header nativo `Authorization: Bearer <jwt>` (el mismo token de `POST /auth/log-in`). \
        Sin token o con token invalido el servidor rechaza la conexion. `/ws` no pasa por el filtro JWT de HTTP; se valida en el CONNECT.
        3. Suscribirse a `/user/queue/notifications`. Cada usuario recibe solo las suyas (el servidor resuelve el destino por el username del JWT).
        4. Cada mensaje es un JSON con la misma forma que la respuesta REST:
        `{ "id": 7, "type": "PROJECT_MENTION", "message": "Fuiste mencionado en el proyecto X", "referenceId": 3, "read": false, "createdAt": "2026-10-01T11:36:58.123" }`

        Si el usuario no esta conectado no se pierde nada: la notificacion queda en BD y se obtiene con `GET /notifications/me` al entrar. \
        Flujo sugerido en el front: al iniciar sesion cargar el historial por REST y luego quedarse escuchando el WebSocket.

        **Limpieza**: se pueden borrar una, varias o todas. Ademas, cada dia a las 3:00 am (hora de Mexico) se borran solas las notificaciones \
        LEIDAS con mas de 30 dias; las no leidas nunca se borran solas.

        **Limitaciones actuales**
        - CORS del WebSocket solo permite el origen `http://localhost:5173`; otro origen (p. ej. el front en prod) no podra conectarse.
        """)
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER', 'COMPANY_ADMIN', 'ADMIN')")
public class NotificationController {

    private final INotificationService notificationService;

    @Operation(summary = "Mis notificaciones", description = "De la mas reciente a la mas antigua. La persona sale del JWT.")
    @ApiResponse(responseCode = "200", description = "Mis notificaciones (array vacio si no tengo)")
    @GetMapping("/notifications/me")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Authentication authentication) {
        return ResponseEntity.ok(notificationService.getMyNotifications(authentication.getName()));
    }

    @Operation(
            summary = "Listar notificaciones por personId (deprecado)",
            description = "Se mantiene mientras el front migra a `GET /notifications/me`. Solo responde si `personId` es el del usuario autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mis notificaciones"),
            @ApiResponse(responseCode = "400", description = "El personId no es el del usuario autenticado")
    })
    @Deprecated
    @GetMapping("/notifications/{personId}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@Parameter(description = "Id de la persona (debe ser el tuyo)") @PathVariable Integer personId,
                                                                       Authentication authentication) {
        return ResponseEntity.ok(notificationService.getNotifications(authentication.getName(), personId));
    }

    @Operation(summary = "Marcar una notificacion mia como leida")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificacion actualizada"),
            @ApiResponse(responseCode = "404", description = "No existe o no es tuya")
    })
    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@Parameter(description = "Id de la notificacion") @PathVariable Integer id,
                                                           Authentication authentication) {
        return ResponseEntity.ok(notificationService.markAsRead(authentication.getName(), id));
    }

    @Operation(summary = "Borrar una notificacion mia", description = "Borrado definitivo.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Borrada"),
            @ApiResponse(responseCode = "404", description = "No existe o no es tuya")
    })
    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(@Parameter(description = "Id de la notificacion") @PathVariable Integer id,
                                                   Authentication authentication) {
        notificationService.deleteMine(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Borrar varias notificaciones mias",
            description = "Body: `{ \"ids\": [3, 7, 9] }` (maximo 200). Los ids ajenos o inexistentes se ignoran sin error. Regresa cuantas se borraron.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Borradas; `object` = cantidad borrada"),
            @ApiResponse(responseCode = "400", description = "Lista vacia o con mas de 200 ids")
    })
    @DeleteMapping("/notifications")
    public ResponseEntity<?> deleteNotifications(@Valid @RequestBody DeleteNotificationsRequest request, Authentication authentication) {
        int deleted = notificationService.deleteMine(authentication.getName(), request.ids());
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje(deleted + " notificaciones borradas")
                .object(deleted).build());
    }

    @Operation(summary = "Limpiar mis notificaciones",
            description = "Con `soloLeidas=true` borra solo las leidas; sin el parametro (o en false) borra TODAS. Regresa cuantas se borraron.")
    @ApiResponse(responseCode = "200", description = "Borradas; `object` = cantidad borrada")
    @DeleteMapping("/notifications/all")
    public ResponseEntity<?> deleteAllNotifications(@Parameter(description = "true = solo las leidas") @RequestParam(defaultValue = "false") boolean soloLeidas,
                                                    Authentication authentication) {
        int deleted = notificationService.deleteAllMine(authentication.getName(), soloLeidas);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje(deleted + " notificaciones borradas")
                .object(deleted).build());
    }
}
