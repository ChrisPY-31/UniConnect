package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.RecomendationDto;
import com.chris.uniconnect.Service.IRecomendationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RecomendationController {

    private final IRecomendationService recomendationService;

    @GetMapping("/recomendation")
    public ResponseEntity<?> getRecomendation() {
        List<RecomendationDto> recomendationDtos = recomendationService.getRecomendations();

        if (recomendationDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recomendationDtos);
    }


    @PostMapping("/recomendation")
    public ResponseEntity<?> createRecomendation(@RequestBody RecomendationDto recomendation) {
        recomendationService.saveRecomendation(recomendation);

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Recomendacion realizada con exito")
                .object(recomendation)
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/recomendation/{id}")
    public ResponseEntity<?> updateRecomendation(@RequestBody RecomendationDto recomendation, @PathVariable int id) {
        RecomendationDto updaterecomendation = null;

        if (recomendationService.existsRecomendation(id) && recomendation != null) {
            updaterecomendation = recomendationService.saveRecomendation(recomendation);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Recomendacion Actualizada con realizada con exito")
                    .object(updaterecomendation)
                    .build(), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/recomendation/{id}")
    public ResponseEntity<?> deleteRecomendation(@PathVariable Integer id) {

        RecomendationDto deleteRecomendation = recomendationService.getIdRecomendation(id);
        if (deleteRecomendation != null) {
            recomendationService.deleteRecomendation(deleteRecomendation);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Recomendacion  con el id: " + id + " no existe")
                .object(null)
                .build(), HttpStatus.OK);

    }


}
