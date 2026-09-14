package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.CareerDto;
import com.chris.uniconnect.Service.ICareerService;
import com.chris.uniconnect.payload.MensajeResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Carreras", description = "Catalogo de carreras universitarias.")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CareerController {

    private ICareerService careerService;

    @Operation(summary = "Listar carreras")
    @ApiResponse(responseCode = "200", description = "Catalogo completo (puede ser un array vacio)")
    @GetMapping("/career")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CareerDto>> getAllCareers (){
        return ResponseEntity.ok(careerService.getCareers());
    }

    @Operation(summary = "Crear carrera", description = "Solo ADMIN. Rechaza nombres duplicados (case-insensitive).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Carrera creada"),
            @ApiResponse(responseCode = "400", description = "Nombre vacio o ya existente"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/career")
    public ResponseEntity<?> saveCareer(@Valid @RequestBody CareerDto newCareer){
        CareerDto savedCareer = careerService.saveCareer(newCareer);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Carrera creada con exito")
                .object(savedCareer).build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar carrera", description = "Solo ADMIN. El body debe incluir idCarrera.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrera actualizada"),
            @ApiResponse(responseCode = "404", description = "No existe una carrera con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/career")
    public ResponseEntity<?> updateCareer(@Valid @RequestBody CareerDto careerDto){
        CareerDto updatedCareer = careerService.updateCareer(careerDto);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Carrera actualizada con exito")
                .object(updatedCareer).build());
    }

    @Operation(summary = "Eliminar carrera", description = "Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminada"),
            @ApiResponse(responseCode = "404", description = "No existe una carrera con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/career/{id}")
    public ResponseEntity<?> deleteCareer(@Parameter(description = "Id de la carrera") @PathVariable Integer id){
        careerService.deleteCareer(id);
        return ResponseEntity.noContent().build();
    }

}
