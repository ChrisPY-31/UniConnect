package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.UbicationDto;
import com.chris.uniconnect.Service.IUbicationService;
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

@Tag(name = "Ubicaciones", description = "Catalogo fijo de los 32 estados de Mexico usado como ubicacion del perfil.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class UbicationController {

    private final IUbicationService ubicationService;

    @Operation(summary = "Listar catalogo de estados")
    @ApiResponse(responseCode = "200", description = "Catalogo completo")
    @GetMapping("/ubication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUbications() {
        return ResponseEntity.ok(ubicationService.getUbications());
    }

    @Operation(summary = "Agregar estados al catalogo", description = "Solo ADMIN. Ignora los nombres que ya existan (case-insensitive).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ubicaciones creadas"),
            @ApiResponse(responseCode = "400", description = "Nombre vacio"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ubication")
    public ResponseEntity<?> createUbication(@Valid @RequestBody List<UbicationDto> ubication) {
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Ubicacion creada con exito")
                .object(ubicationService.createUbication(ubication))
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar un estado del catalogo", description = "Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe una ubicacion con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/ubication/{id}")
    public ResponseEntity<?> deleteUbication(@Parameter(description = "Id de la ubicacion") @PathVariable Integer id) {
        if (!ubicationService.existUbication(id)) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("La ubicacion con el id: " + id + " no existe")
                    .object(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        ubicationService.deleteUbication(id);
        return ResponseEntity.noContent().build();
    }
}
