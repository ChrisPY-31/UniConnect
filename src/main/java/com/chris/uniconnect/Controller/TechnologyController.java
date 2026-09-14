package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.TechnologyDto;
import com.chris.uniconnect.Service.ITechnologyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tecnologias", description = "Catalogo de tecnologias que un estudiante puede asociar a un proyecto.")
@RestController()
@RequestMapping("api/v1")
@AllArgsConstructor
public class TechnologyController {

    private final ITechnologyService technologyService;

    @Operation(summary = "Listar catalogo de tecnologias")
    @ApiResponse(responseCode = "200", description = "Catalogo completo")
    @GetMapping("/technology")
    public ResponseEntity<?> getTechnologies() {
        return ResponseEntity.ok(technologyService.getTechnologies());
    }

    @Operation(summary = "Agregar tecnologias al catalogo", description = "Solo ADMIN. Ignora los nombres que ya existan (case-insensitive).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tecnologias creadas"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/technology")
    public ResponseEntity<?> saveTechnology(@RequestBody List<TechnologyDto> technology) {
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Tecnologia creada con exito")
                .object(technologyService.createTechnology(technology))
                .build(), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Agregar tecnologias nuevas (no renombra existentes)",
            description = "Pese al verbo PUT, internamente solo inserta los nombres que todavia no existen en el catalogo — no modifica el nombre de una tecnologia ya creada. Solo ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tecnologias nuevas insertadas (las repetidas se ignoran en silencio)"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
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

    @Operation(summary = "Eliminar una tecnologia del catalogo", description = "Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eliminada"),
            @ApiResponse(responseCode = "404", description = "No existe una tecnologia con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/technology/{id}")
    public ResponseEntity<?> deleteTechnology(@Parameter(description = "Id de la tecnologia") @PathVariable Integer id) {
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
