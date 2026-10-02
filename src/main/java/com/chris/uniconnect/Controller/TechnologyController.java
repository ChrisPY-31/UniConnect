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

    @Operation(summary = "Renombrar una tecnologia del catalogo", description = "Solo ADMIN. El id va en la ruta; el body solo necesita nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tecnologia actualizada"),
            @ApiResponse(responseCode = "400", description = "Nombre vacio o ya usado por otra tecnologia"),
            @ApiResponse(responseCode = "404", description = "No existe una tecnologia con ese id"),
            @ApiResponse(responseCode = "403", description = "No es ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/technology/{id}")
    public ResponseEntity<?> updateTechnology(@Parameter(description = "Id de la tecnologia") @PathVariable Integer id,
                                              @RequestBody TechnologyDto technology) {
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Tecnologia actualizada con exito")
                .object(technologyService.updateTechnology(id, technology))
                .build());
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
