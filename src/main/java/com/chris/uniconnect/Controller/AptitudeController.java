package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.AptitudeDto;
import com.chris.uniconnect.Service.IAptitudeService;
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

@Tag(name = "Aptitudes", description = "Catalogo fijo de 20 aptitudes que una persona puede elegir (maximo 5) para su perfil.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class AptitudeController {

    private final IAptitudeService aptitudeService;

    @Operation(summary = "Listar catalogo de aptitudes")
    @ApiResponse(responseCode = "200", description = "Catalogo completo")
    @GetMapping("/aptitude")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAptitudes() {
        return ResponseEntity.ok(aptitudeService.getAptitudes());
    }

    @Operation(summary = "Agregar aptitudes al catalogo", description = "Solo ADMIN. Ignora los nombres que ya existan en el catalogo (case-insensitive).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aptitudes creadas"),
            @ApiResponse(responseCode = "400", description = "Nombre vacio o invalido"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/aptitude")
    public ResponseEntity<?> createAptitude(@Valid @RequestBody List<AptitudeDto> aptitude) {
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Aptitud creada con exito")
                .object(aptitudeService.createAptitud(aptitude))
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar una aptitud del catalogo", description = "Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminada"),
            @ApiResponse(responseCode = "404", description = "No existe una aptitud con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/aptitude/{id}")
    public ResponseEntity<?> deleteAptitude(@Parameter(description = "Id de la aptitud") @PathVariable Integer id) {
        if (!aptitudeService.existAptitude(id)) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("La aptitud con el id: " + id + " no existe")
                    .object(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        aptitudeService.deleteAptitude(id);
        return ResponseEntity.noContent().build();
    }
}
