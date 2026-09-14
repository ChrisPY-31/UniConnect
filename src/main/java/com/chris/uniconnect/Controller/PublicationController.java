package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.PublicationDto;
import com.chris.uniconnect.Service.IPublicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Publicaciones", description = "Feed de publicaciones de perfil. El autor (idPersona) se resuelve por JWT en create/update/delete.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PublicationController {

    private IPublicationService publicationService;

    @Operation(summary = "Listar todas las publicaciones")
    @ApiResponse(responseCode = "200", description = "Feed completo")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/publication")
    public ResponseEntity<?> getAllPublications() {
        return new ResponseEntity<>(publicationService.getAllPublications(), HttpStatus.OK);
    }

    @Operation(summary = "Crear una publicacion")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Publicacion creada")
    })
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PostMapping("/publication")
    public ResponseEntity<?> createPublication(@RequestBody PublicationDto publication, Authentication authentication) {
        PublicationDto savePublication = publicationService.createPublication(authentication.getName(), publication);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Publicacion creada con exito")
                .object(savePublication)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una publicacion propia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizada"),
            @ApiResponse(responseCode = "400", description = "La publicacion pertenece a otra persona"),
            @ApiResponse(responseCode = "404", description = "No existe una publicacion con ese id")
    })
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PutMapping("/publication/{id}")
    public ResponseEntity<?> updatePublication(@Parameter(description = "Id de la publicacion") @PathVariable Integer id,
                                                @RequestBody PublicationDto publication,
                                                Authentication authentication) {
        PublicationDto publicationUpdate = publicationService.updatePublication(authentication.getName(), id, publication);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Publicacion actualizada con exito")
                .object(publicationUpdate)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar una publicacion propia")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminada"),
            @ApiResponse(responseCode = "400", description = "La publicacion pertenece a otra persona"),
            @ApiResponse(responseCode = "404", description = "No existe una publicacion con ese id")
    })
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @DeleteMapping("/publication/{id}")
    public ResponseEntity<?> deletePublication(@Parameter(description = "Id de la publicacion") @PathVariable Integer id, Authentication authentication) {
        publicationService.deletePublication(authentication.getName(), id);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Publicacion eliminada")
                .build(), HttpStatus.NO_CONTENT);
    }

}
