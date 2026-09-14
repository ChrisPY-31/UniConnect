package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.PublicationInteractionDto;
import com.chris.uniconnect.Service.IPublicationInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Interacciones de publicacion", description = "Likes y comentarios sobre una publicacion. El idPerson de la interaccion se resuelve por JWT, no por el body.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PublicationInteractionController {

    private final IPublicationInteractionService publicationInteractionService;

    @Operation(summary = "Listar todas las interacciones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Interacciones encontradas"),
            @ApiResponse(responseCode = "204", description = "No hay interacciones")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/AllPublication")
    public ResponseEntity<?> AllPublication() {
        List<PublicationInteractionDto> publicationInteractionDto = publicationInteractionService.getPublications();

        if (!publicationInteractionDto.isEmpty()) {
            return ResponseEntity.ok(publicationInteractionDto);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Dar like/comentar una publicacion",
            description = "El body debe traer id.idPublication. Hace upsert: si ya existias en esta publicacion, actualiza tu interaccion en vez de duplicarla."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Interaccion guardada"),
            @ApiResponse(responseCode = "400", description = "Falta id.idPublication"),
            @ApiResponse(responseCode = "404", description = "La publicacion no existe")
    })
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PostMapping("/interactionPublicacion")
    public ResponseEntity<?> savePublication(@RequestBody PublicationInteractionDto publicationInteractionDto,
                                              Authentication authentication) {
        PublicationInteractionDto saved = publicationInteractionService.createPublicationUsers(
                authentication.getName(), publicationInteractionDto);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Publicacion creada con exito")
                .object(saved)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar mi interaccion sobre una publicacion", description = "Mismo comportamiento de upsert que el POST.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Interaccion actualizada"),
            @ApiResponse(responseCode = "400", description = "Falta id.idPublication"),
            @ApiResponse(responseCode = "404", description = "La publicacion no existe")
    })
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PutMapping("/interactionPublicacion")
    public ResponseEntity<?> updatePublication(@RequestBody PublicationInteractionDto publicationInteractionDto,
                                                Authentication authentication) {
        PublicationInteractionDto updated = publicationInteractionService.createPublicationUsers(
                authentication.getName(), publicationInteractionDto);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Publicacion actualizada con exito")
                .object(updated)
                .build(), HttpStatus.OK);
    }

}
