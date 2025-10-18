package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.UbicationDto;
import com.chris.uniconnect.Service.IUbicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class UbicationController {

    private final IUbicationService ubicationService;

    @PostMapping("/ubication")
    private ResponseEntity<?> createUbication(@RequestBody UbicationDto ubication) {
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("Ubicacion creada con exito").object(ubicationService.createUbication(ubication)).build(), HttpStatus.CREATED);
    }

    @PutMapping("/ubication")
    private ResponseEntity<?> updateUbication(@RequestBody UbicationDto ubication) {
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("Ubicacion actualizada con exito").object(ubicationService.updateUbication(ubication)).build(), HttpStatus.CREATED);
    }

    @DeleteMapping("/ubication/{id}")
    private ResponseEntity<?> deleteUbication(@PathVariable Integer id) {

        boolean existUbication = ubicationService.existUbication(id);
        if (existUbication) {
            ubicationService.deleteUbication(id);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("Ubicacion creada con exito").build(), HttpStatus.NO_CONTENT);

        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("La ubicacion con el id: "+ id +" no existe").object(existUbication).build(), HttpStatus.NOT_FOUND);
    }
}
