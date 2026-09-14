package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.ProjectLinksDto;
import com.chris.uniconnect.Service.IProjectLinkService;
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

@Tag(name = "Links de proyecto", description = "URLs asociadas a un proyecto (GitHub, deploy, etc). Solo el dueno del proyecto puede administrarlas.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class ProjectLinkController {

    private final IProjectLinkService projectLinkService;

    @Operation(summary = "Agregar un link a un proyecto propio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Link creado"),
            @ApiResponse(responseCode = "400", description = "El proyecto no es tuyo"),
            @ApiResponse(responseCode = "404", description = "El proyecto no existe")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/projectLink")
    public ResponseEntity<?> saveProjectLink(@RequestBody ProjectLinksDto projectLink, Authentication authentication) {

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("redes creada con exito")
                .object(projectLinkService.createProjectLinks(authentication.getName(), projectLink))
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un link propio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado"),
            @ApiResponse(responseCode = "400", description = "El link o el nuevo proyecto no son tuyos"),
            @ApiResponse(responseCode = "404", description = "El link o el proyecto no existen")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/projectLink/{id}")
    public ResponseEntity<?> updateProjectLink(@RequestBody ProjectLinksDto projectLink,
                                                @Parameter(description = "Id del link") @PathVariable Integer id,
                                                Authentication authentication) {

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Red actualizada con exito")
                .object(projectLinkService.updateProjectLinks(authentication.getName(), id, projectLink))
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un link propio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Eliminado"),
            @ApiResponse(responseCode = "400", description = "El link no es tuyo"),
            @ApiResponse(responseCode = "404", description = "El link no existe")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/projectLink/{id}")
    public ResponseEntity<?> deleteProjectLink(@Parameter(description = "Id del link") @PathVariable Integer id, Authentication authentication) {
        ProjectLinksDto projectLink = projectLinkService.getProjectLinkById(id);

        if (projectLink == null) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("El id " + id + " No existe")
                    .object(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        projectLinkService.deleteProjectLinks(authentication.getName(), id);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Red eliminado con exito")
                .object(projectLink)
                .build(), HttpStatus.OK);
    }
}
