package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.PublicationDto;
import com.chris.uniconnect.Service.IPublicationService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PublicationController {

    private IPublicationService publicationService;

    @GetMapping("/publication")
    public ResponseEntity<?> getAllPublications() {
        return new ResponseEntity<>(publicationService.getAllPublications(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PostMapping("/publication")
    public ResponseEntity<?> createPublication(@RequestBody PublicationDto publication) {
        PublicationDto savePublication = null;
        try {
            savePublication = publicationService.createPublication(publication);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("Publicacion creada con exito").object(savePublication).build(), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(MensajeResponse.builder().mensaje(e.getMessage()).object(savePublication).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PutMapping("/publication/{id}")
    public ResponseEntity<?> updatePublication(@PathVariable Integer id, @RequestBody PublicationDto publication) {

        PublicationDto publicationUpdate = null;
        try {
            if (publicationService.existsPublication(id)) {
                publicationUpdate = publicationService.createPublication(publication);
                return new ResponseEntity<>(MensajeResponse.builder().mensaje("Publicacion Actualizada con exito").object(publicationUpdate).build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(MensajeResponse.builder().mensaje("El id no existe").object(publicationUpdate).build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {

            return ResponseEntity.ok(MensajeResponse.builder().mensaje(e.getMessage()).object(publicationUpdate).build());
        }

    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @DeleteMapping("/publication/{id}")
    public ResponseEntity<?> deletePublication(@PathVariable Integer id) {
        try {
            PublicationDto publicationDelete = publicationService.publicationById(id);
            publicationService.deletePublication(publicationDelete);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Publication eliminado")
                    .build(), HttpStatus.NO_CONTENT);

        } catch (DataAccessException e) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(e.getMessage())
                            .object(null)
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

}
