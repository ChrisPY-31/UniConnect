package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Entity.Recruiter;
import com.chris.uniconnect.Service.IRecruiterServce;
import com.chris.uniconnect.payload.MensajeResponse;
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
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Reclutadores", description = "Perfil de Recruiter. PENDIENTE: update/delete no validan que el {id} sea el del reclutador autenticado; delete tampoco borra el registro todavia.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterController {

    private final IRecruiterServce recruiterService;

    @Operation(summary = "Crear un reclutador")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reclutador creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping("/recruiter")
    public ResponseEntity<?> createRecuiteer(@RequestBody RecruiterDto recruiter) {
        try {
            RecruiterDto createRecuiter = recruiterService.createRecruiter(recruiter);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("cuenta creada con exito")
                    .object(createRecuiter)
                    .build(), HttpStatus.CREATED
            );
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            summary = "Actualizar el perfil de un reclutador",
            description = "ADVERTENCIA: el {id} del path no se valida contra el usuario autenticado — cualquier RECRUITER puede editar el perfil de otro. Pendiente de cerrar."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "404", description = "No existe un reclutador con ese id")
    })
    @PutMapping("/recruiter/{id}")
    public ResponseEntity<?> updateRecuiteer(@RequestBody RecruiterDto recruiter, @Parameter(description = "Id del reclutador") @PathVariable Integer id) {

        RecruiterDto updateRecruiter = null;
        try {

            if (!recruiterService.existRecruiter(id)) {
                throw new ResourceNotFoundException("recuiter", "id", id);
            }
            recruiter.setId(id);
            updateRecruiter = recruiterService.updateRecruiter(recruiter);
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("peticion actualizada con exito")
                            .object(updateRecruiter)
                            .build(), HttpStatus.OK
            );

        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    @Operation(
            summary = "Eliminar un reclutador",
            description = "ADVERTENCIA: el {id} no se valida contra el usuario autenticado, y el metodo todavia no elimina el registro — solo lo busca y devuelve un mensaje de exito. Pendiente de implementar."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Respuesta de exito (el registro no se borra realmente todavia)"),
            @ApiResponse(responseCode = "404", description = "No existe un reclutador con ese id")
    })
    @DeleteMapping("/recruiter/{id}")
    public ResponseEntity<?> deleteRecuiter(@Parameter(description = "Id del reclutador") @PathVariable Integer id) {

        if(!recruiterService.existRecruiter(id)) {
            throw new ResourceNotFoundException("recuiter", "id", id);
        }
        RecruiterDto deleteRecruiter = recruiterService.getRecruiterById(id);

        return  new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Cuenta elimina con exito")
                        .object(deleteRecruiter)
                        .build(), HttpStatus.OK
        );
    }

}
