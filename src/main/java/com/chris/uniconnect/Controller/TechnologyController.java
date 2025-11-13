package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.TechnologyDto;
import com.chris.uniconnect.Service.ITechnologyService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/v1")
@AllArgsConstructor
public class TechnologyController {

    private final ITechnologyService technologyService;

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PostMapping("/technology")
    public ResponseEntity<?> saveTechnology(@RequestBody List<TechnologyDto> technology) {
        System.out.println("aqui tecnologias");
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Tecnologia creada con exito")
                .object(technologyService.createTechnology(technology))
                .build(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("/technology")
    public ResponseEntity<?> updateTechnology(@RequestBody List<TechnologyDto> technology) {
        try {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Tecnologia creada con exito")
                    .object(technologyService.createTechnology(technology))
                    .build(), HttpStatus.OK);

        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @DeleteMapping("/technology/{id}")
    public ResponseEntity<?> deleteTechnology(@PathVariable Integer id) {
        if (technologyService.existTechnology(id)) {
            technologyService.deleteTechnology(id);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Tecnologia creada con exito")
                    .object(null)
                    .build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Tecnologia con el id: " + id + " no existe")
                .object(null)
                .build(), HttpStatus.NOT_FOUND);
    }
}
