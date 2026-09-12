package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.RecomendationDto;
import com.chris.uniconnect.Model.Entity.RecomendationPk;
import com.chris.uniconnect.Service.INotificationService;
import com.chris.uniconnect.Service.IRecomendationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RecomendationController {

    private final IRecomendationService recomendationService;

    private final INotificationService notificationService;

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
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
        RecomendationDto saved = recomendationService.saveRecomendation(recomendation);
        notificationService.notifyRecommendation(saved.getId());
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Recomendacion realizada con exito")
                .object(saved)
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/recomendation/{idStudent}/{idTeacher}")
    public ResponseEntity<?> updateRecomendation(@RequestBody RecomendationDto recomendation, @PathVariable Integer idStudent, @PathVariable Integer idTeacher) {
        RecomendationPk id = new RecomendationPk(idStudent, idTeacher);

        if (recomendationService.existsRecomendation(id) && recomendation != null) {
            recomendation.setId(id);
            RecomendationDto updaterecomendation = recomendationService.saveRecomendation(recomendation);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Recomendacion Actualizada con realizada con exito")
                    .object(updaterecomendation)
                    .build(), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/recomendation/{idStudent}/{idTeacher}")
    public ResponseEntity<?> deleteRecomendation(@PathVariable Integer idStudent, @PathVariable Integer idTeacher) {
        RecomendationPk id = new RecomendationPk(idStudent, idTeacher);

        RecomendationDto deleteRecomendation = recomendationService.getIdRecomendation(id);
        if (deleteRecomendation != null) {
            recomendationService.deleteRecomendation(deleteRecomendation);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Recomendacion con los ids: " + idStudent + ", " + idTeacher + " no existe")
                .object(null)
                .build(), HttpStatus.OK);

    }


}
