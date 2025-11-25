package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.EducationDto;
import com.chris.uniconnect.Service.IEducationService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor

@PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
public class EducationController {

    private IEducationService educationService;

    @PostMapping("/education")
    public ResponseEntity<?> createEducation(@RequestBody EducationDto education) {

        try {
            return new ResponseEntity<>(MensajeResponse.builder().object(educationService.create(education)).mensaje("Educacion creado con exito").build(), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/education/{id}")
    public ResponseEntity<?> updateEducation(@RequestBody EducationDto education, @PathVariable int id) {

        try {
            EducationDto updateEducation = null;
            if (!educationService.exitEducation(id) && !education.getIdEducacion().equals(id)) {
                throw new ResourceNotFoundException("Error la educacion y la path debe ser igual al idEducacion");
            }
            updateEducation = educationService.create(education);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("Educacion actualizada con exito.").object(updateEducation).build(), HttpStatus.OK);

        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<?> deleteEducation(@PathVariable Integer id) {

        try {
            if (educationService.exitEducation(id)) {
                educationService.deleteEducation(id);
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Educacion eliminado con exito").build(), HttpStatus.OK);
            }
            throw new ResourceNotFoundException("id", "educacion", id);

        } catch (Exception e) {
            // Log del error real
            System.err.println("Error al eliminar educación: " + e.getMessage());
            e.printStackTrace();

            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Error interno del servidor al eliminar educación")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
