package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.RecomendationDto;
import com.chris.uniconnect.Service.INotificationService;
import com.chris.uniconnect.Service.IRecomendationService;
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

import java.util.List;

@Tag(name = "Recomendaciones", description = "Carta de recomendacion de un TEACHER hacia un STUDENT (una por par profesor-estudiante). El idTeacher se resuelve por JWT.")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RecomendationController {

    private final IRecomendationService recomendationService;

    private final INotificationService notificationService;

    @Operation(summary = "Listar todas las recomendaciones")
    @ApiResponse(responseCode = "200", description = "Lista completa (vacia si no hay ninguna)")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @GetMapping("/recomendation")
    public ResponseEntity<?> getRecomendation() {
        List<RecomendationDto> recomendationDtos = recomendationService.getRecomendations();
        return ResponseEntity.ok(recomendationDtos);
    }

    @Operation(
            summary = "Recomendar a un estudiante",
            description = "Solo TEACHER. El id.idStudent va en el body; el id.idTeacher se ignora y se toma del profesor autenticado. Dispara una notificacion al estudiante."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recomendacion creada"),
            @ApiResponse(responseCode = "400", description = "Falta id.idStudent"),
            @ApiResponse(responseCode = "404", description = "El estudiante no existe")
    })
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/recomendation")
    public ResponseEntity<?> createRecomendation(@RequestBody RecomendationDto recomendation, Authentication authentication) {
        RecomendationDto saved = recomendationService.createRecomendation(authentication.getName(), recomendation);
        notificationService.notifyRecommendation(saved.getId());
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Recomendacion realizada con exito")
                .object(saved)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar mi recomendacion a un estudiante", description = "Solo el TEACHER autenticado, sobre su propia recomendacion a ese estudiante.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizada"),
            @ApiResponse(responseCode = "404", description = "No existe una recomendacion tuya para ese estudiante")
    })
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/recomendation/{idStudent}")
    public ResponseEntity<?> updateRecomendation(@RequestBody RecomendationDto recomendation,
                                                  @Parameter(description = "Id del estudiante recomendado") @PathVariable Integer idStudent,
                                                  Authentication authentication) {
        RecomendationDto updated = recomendationService.updateRecomendation(authentication.getName(), idStudent, recomendation);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Recomendacion actualizada con exito")
                .object(updated)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar mi recomendacion a un estudiante")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminada"),
            @ApiResponse(responseCode = "404", description = "No existe una recomendacion tuya para ese estudiante")
    })
    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/recomendation/{idStudent}")
    public ResponseEntity<?> deleteRecomendation(@Parameter(description = "Id del estudiante recomendado") @PathVariable Integer idStudent,
                                                  Authentication authentication) {
        recomendationService.deleteRecomendation(authentication.getName(), idStudent);
        return ResponseEntity.noContent().build();
    }

}
