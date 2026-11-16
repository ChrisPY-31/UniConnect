package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.ConversationDto;
import com.chris.uniconnect.Model.Dto.MessageDto;
import com.chris.uniconnect.Model.Dto.SendMessageRequest;
import com.chris.uniconnect.Model.Dto.StartConversationRequest;
import com.chris.uniconnect.Service.IChatService;
import com.chris.uniconnect.payload.MensajeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import com.chris.uniconnect.util.PageRequests;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = """
        Chat estudiante-reclutador. Una conversacion por par; solo el reclutador la inicia, directo con cualquier estudiante (no hace falta que se haya postulado). \
        Ya abierta, ambos escriben. Solo el estudiante puede bloquearla (BLOQUEADA = nadie envia).

        Envio por REST; tiempo real por WebSocket: suscribirse a `/user/queue/messages` (misma conexion STOMP `/ws` con JWT en el CONNECT). \
        Cada mensaje enviado, editado o eliminado se empuja a los dos participantes como MessageDto (incluye idConversacion); \
        el campo `evento` (NUEVO, EDITADO, ELIMINADO) dice que paso. Las fechas salen con zona horaria. \
        Una conversacion ajena responde 404.""")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT', 'RECRUITER')")
public class ChatController {

    private final IChatService chatService;

    @Operation(summary = "Iniciar una conversacion con un estudiante",
            description = "Solo RECRUITER. Body: idEstudiante + mensaje; el estudiante no necesita haberse postulado. " +
                    "Si ya existe conversacion con ese estudiante, se reutiliza y se le agrega el mensaje. " +
                    "El campo vacante del resultado solo viene en conversaciones antiguas abiertas desde una postulacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mensaje enviado, regresa la conversacion"),
            @ApiResponse(responseCode = "400", description = "Conversacion bloqueada por el estudiante o mensaje invalido"),
            @ApiResponse(responseCode = "404", description = "No existe el estudiante o su cuenta esta deshabilitada"),
            @ApiResponse(responseCode = "403", description = "No es RECRUITER")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/recruiter/me/conversations")
    public ResponseEntity<?> startConversation(@Valid @RequestBody StartConversationRequest request, Authentication authentication) {
        ConversationDto conversation = chatService.startConversation(authentication.getName(), request);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Mensaje enviado con exito")
                .object(conversation).build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Mi bandeja de conversaciones", description = "STUDENT o RECRUITER. De la mas reciente a la mas antigua, con ultimo mensaje y no leidos.")
    @ApiResponse(responseCode = "200", description = "Lista de conversaciones (puede ser un array vacio)")
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDto>> getMyConversations(Authentication authentication) {
        return ResponseEntity.ok(chatService.getMyConversations(authentication.getName()));
    }

    @Operation(summary = "Detalle de una conversacion mia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conversacion encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe o no participas en ella")
    })
    @GetMapping("/conversations/{id}")
    public ResponseEntity<ConversationDto> getConversation(@Parameter(description = "Id de la conversacion") @PathVariable Long id,
                                                           Authentication authentication) {
        return ResponseEntity.ok(chatService.getConversation(authentication.getName(), id));
    }

    @Operation(summary = "Mensajes de una conversacion mia", description = "Paginado, del mas reciente al mas antiguo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagina de mensajes"),
            @ApiResponse(responseCode = "400", description = "page < 0 o size fuera de 1..50"),
            @ApiResponse(responseCode = "404", description = "No existe o no participas en ella")
    })
    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<Page<MessageDto>> getMessages(@Parameter(description = "Id de la conversacion") @PathVariable Long id,
                                                        @Parameter(description = "Numero de pagina, desde 0") @RequestParam(defaultValue = "0") int page,
                                                        @Parameter(description = "Tamano de pagina (1..50)") @RequestParam(defaultValue = "30") int size,
                                                        Authentication authentication) {
        return ResponseEntity.ok(chatService.getMessages(authentication.getName(), id, PageRequests.of(page, size)));
    }

    @Operation(summary = "Enviar un mensaje", description = "STUDENT o RECRUITER participante. Se empuja por WebSocket a ambos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mensaje enviado"),
            @ApiResponse(responseCode = "400", description = "Conversacion bloqueada o mensaje invalido"),
            @ApiResponse(responseCode = "404", description = "No existe o no participas en ella")
    })
    @PostMapping("/conversations/{id}/messages")
    public ResponseEntity<MessageDto> sendMessage(@Parameter(description = "Id de la conversacion") @PathVariable Long id,
                                                  @Valid @RequestBody SendMessageRequest request,
                                                  Authentication authentication) {
        return new ResponseEntity<>(chatService.sendMessage(authentication.getName(), id, request), HttpStatus.CREATED);
    }

    @Operation(summary = "Editar un mensaje propio",
            description = "STUDENT o RECRUITER, solo el autor y durante los primeros 15 minutos (ver `editableHasta`). Aunque ya se haya leido; queda con fecha en `editado`. Si el texto no cambia, no se marca como editado ni se empuja. " +
                    "El mensaje actualizado se empuja por WebSocket a ambos (reemplazar por idMensaje).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mensaje editado"),
            @ApiResponse(responseCode = "400", description = "Paso el tiempo para editar, mensaje eliminado, conversacion bloqueada o contenido invalido"),
            @ApiResponse(responseCode = "404", description = "No existe la conversacion o el mensaje no es tuyo")
    })
    @PutMapping("/conversations/{id}/messages/{idMensaje}")
    public ResponseEntity<MessageDto> editMessage(@Parameter(description = "Id de la conversacion") @PathVariable Long id,
                                                  @Parameter(description = "Id del mensaje") @PathVariable Long idMensaje,
                                                  @Valid @RequestBody SendMessageRequest request,
                                                  Authentication authentication) {
        return ResponseEntity.ok(chatService.editMessage(authentication.getName(), id, idMensaje, request));
    }

    @Operation(summary = "Eliminar un mensaje propio",
            description = "STUDENT o RECRUITER, solo el autor, sin limite de tiempo. Borrado logico: el mensaje se queda con `eliminado = true` " +
                    "y `contenido = null` (mostrar \"Este mensaje fue eliminado\"). Se empuja por WebSocket a ambos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mensaje eliminado, regresa el mensaje con eliminado = true"),
            @ApiResponse(responseCode = "400", description = "Ya estaba eliminado o la conversacion esta bloqueada"),
            @ApiResponse(responseCode = "404", description = "No existe la conversacion o el mensaje no es tuyo")
    })
    @DeleteMapping("/conversations/{id}/messages/{idMensaje}")
    public ResponseEntity<MessageDto> deleteMessage(@Parameter(description = "Id de la conversacion") @PathVariable Long id,
                                                    @Parameter(description = "Id del mensaje") @PathVariable Long idMensaje,
                                                    Authentication authentication) {
        return ResponseEntity.ok(chatService.deleteMessage(authentication.getName(), id, idMensaje));
    }

    @Operation(summary = "Marcar como leida", description = "Marca como leidos los mensajes de la otra parte en esta conversacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conversacion con noLeidos = 0"),
            @ApiResponse(responseCode = "404", description = "No existe o no participas en ella")
    })
    @PatchMapping("/conversations/{id}/read")
    public ResponseEntity<ConversationDto> markAsRead(@Parameter(description = "Id de la conversacion") @PathVariable Long id,
                                                      Authentication authentication) {
        return ResponseEntity.ok(chatService.markAsRead(authentication.getName(), id));
    }

    @Operation(summary = "Bloquear o desbloquear una conversacion", description = "Solo STUDENT. Bloqueada, nadie puede enviar mensajes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Ya estaba en ese estado"),
            @ApiResponse(responseCode = "404", description = "No existe o no participas en ella"),
            @ApiResponse(responseCode = "403", description = "No es STUDENT")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/conversations/{id}/block")
    public ResponseEntity<?> changeBlock(@Parameter(description = "Id de la conversacion") @PathVariable Long id,
                                         @Parameter(description = "true = bloquear, false = desbloquear") @RequestParam boolean bloqueada,
                                         Authentication authentication) {
        ConversationDto conversation = chatService.changeBlock(authentication.getName(), id, bloqueada);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje(bloqueada ? "Conversacion bloqueada" : "Conversacion desbloqueada")
                .object(conversation).build());
    }
}
