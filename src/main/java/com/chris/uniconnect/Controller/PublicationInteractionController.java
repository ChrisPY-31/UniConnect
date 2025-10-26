package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.PublicationInteractionDto;
import com.chris.uniconnect.Service.IPublicationInteractionService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PublicationInteractionController {

    private final IPublicationInteractionService publicationInteractionService;

    @GetMapping("/AllPublication")
    public ResponseEntity<?> AllPublication() {
        List<PublicationInteractionDto> publicationInteractionDto = publicationInteractionService.getPublications();

        if (!publicationInteractionDto.isEmpty()) {
            return ResponseEntity.ok(publicationInteractionDto);
        }
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PostMapping("/interactionPublicacion")
    public ResponseEntity<?> savePublication(@RequestBody PublicationInteractionDto publicationInteractionDto) {
        PublicationInteractionDto publicationInteractionDto1 = null;

        try {
            publicationInteractionDto1 = publicationInteractionService.createPublicationUsers(publicationInteractionDto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Publicacion creada con exito")
                    .object(publicationInteractionDto1)
                    .build(), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PutMapping("/interactionPublicacion")
    public ResponseEntity<?> updatePublication(@RequestBody PublicationInteractionDto publicationInteractionDto) {
        PublicationInteractionDto publicationInteractionDto1 = null;
        try {
            boolean exit = publicationInteractionService.existPublicatio(publicationInteractionDto.getId().getIdPublication());
            if (exit) {
                publicationInteractionDto1 = publicationInteractionService.createPublicationUsers(publicationInteractionDto);
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Publicacion actualizada con exio")
                        .object(publicationInteractionDto1)
                        .build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("El id no se encuentra en la base de datos")
                        .object(publicationInteractionDto1)
                        .build(), HttpStatus.OK);
            }
        } catch (DataAccessException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}


