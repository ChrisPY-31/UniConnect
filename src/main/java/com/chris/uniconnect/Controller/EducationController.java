package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.EducationDto;
import com.chris.uniconnect.Service.IEducationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Educacion", description = "Historial academico (institucion, grado, fechas) del perfil autenticado. El idPersona se resuelve por JWT, no por el body.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
public class EducationController {

    private IEducationService educationService;

    @Operation(summary = "Agregar un registro de educacion a mi perfil")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Educacion creada"),
            @ApiResponse(responseCode = "400", description = "Institucion/grado/tipo/fecha de inicio faltantes")
    })
    @PostMapping("/education")
    public ResponseEntity<?> createEducation(@Valid @RequestBody EducationDto education, Authentication authentication) {
        EducationDto created = educationService.create(authentication.getName(), education);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Educacion creada con exito")
                .object(created)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un registro de educacion propio", description = "Solo el dueno del registro puede editarlo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o el registro pertenece a otra persona"),
            @ApiResponse(responseCode = "404", description = "No existe una educacion con ese id")
    })
    @PutMapping("/education/{id}")
    public ResponseEntity<?> updateEducation(@Parameter(description = "Id del registro de educacion") @PathVariable Integer id,
                                              @Valid @RequestBody EducationDto education,
                                              Authentication authentication) {
        EducationDto updated = educationService.updateEducation(authentication.getName(), id, education);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Educacion actualizada con exito")
                .object(updated)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un registro de educacion propio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eliminado"),
            @ApiResponse(responseCode = "400", description = "El registro pertenece a otra persona"),
            @ApiResponse(responseCode = "404", description = "No existe una educacion con ese id")
    })
    @DeleteMapping("/education/{id}")
    public ResponseEntity<?> deleteEducation(@Parameter(description = "Id del registro de educacion") @PathVariable Integer id,
                                              Authentication authentication) {
        educationService.deleteEducation(authentication.getName(), id);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Educacion eliminada con exito")
                .build(), HttpStatus.OK);
    }
}
