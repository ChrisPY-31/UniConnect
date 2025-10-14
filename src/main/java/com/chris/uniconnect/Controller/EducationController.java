package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exeptions.Mensaje;
import com.chris.uniconnect.Model.Dto.EducationDto;
import com.chris.uniconnect.Service.IEducationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class EducationController {

    private IEducationService educationService;

    @PostMapping("/education")
    public ResponseEntity<?> createEducation(@RequestBody EducationDto education) {

        try {
            return new ResponseEntity<>(Mensaje.builder().object(educationService.create(education)).mensaje("Educacion creado con exito").build(), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("education/{id}")
    public ResponseEntity<?> deleteEducation(@PathVariable Integer id) {

        boolean existEducationId = educationService.exitEducation(id);

        if (existEducationId) {
            return new ResponseEntity<>(Mensaje.builder().mensaje("Educacion elimando con exito").build(), HttpStatus.OK);

        }
        return new ResponseEntity<>(Mensaje.builder().mensaje("La educacion con el id: " + id + " no existe").build(), HttpStatus.NOT_FOUND);
    }
}
