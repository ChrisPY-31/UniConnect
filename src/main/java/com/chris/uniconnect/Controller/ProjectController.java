package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Service.IProjectService;
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

@Tag(name = "Proyectos", description = "Portafolio de proyectos de un estudiante. El dueno (idEstudiante) se resuelve por JWT en create/update/delete.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor

public class ProjectController {

    private final IProjectService projectService;

    @Operation(summary = "Listar todos los proyectos")
    @ApiResponse(responseCode = "200", description = "Lista de proyectos (vacia si no hay ninguno)")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/projects")
    public ResponseEntity<?> getProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        if (!projects.isEmpty()) {
            return new ResponseEntity<>(MensajeResponse.builder().object(projects).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().object(projects).build(), HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Crear un proyecto",
            description = "Solo STUDENT. El idEstudiante se toma del usuario autenticado, no del body. Las menciones y tecnologias deben traer ids reales ya existentes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Proyecto creado"),
            @ApiResponse(responseCode = "400", description = "Nombre vacio"),
            @ApiResponse(responseCode = "404", description = "Alguna mencion o tecnologia no existe")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/projects")
    public ResponseEntity<?> saveProject(@RequestBody ProjectDto projectDto, Authentication authentication) {
        ProjectDto project = projectService.createProject(authentication.getName(), projectDto);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Proyecto guardado correctamente")
                .object(project)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un proyecto propio", description = "El body debe incluir idProject. Solo el dueno puede editarlo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proyecto actualizado"),
            @ApiResponse(responseCode = "400", description = "El proyecto pertenece a otro estudiante"),
            @ApiResponse(responseCode = "404", description = "No existe el proyecto, mencion o tecnologia")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/projects")
    public ResponseEntity<?> updateProject(@RequestBody ProjectDto projectDto, Authentication authentication) {
        ProjectDto project = projectService.updateProject(authentication.getName(), projectDto);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Proyecto actualizado correctamente")
                .object(project)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un proyecto propio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado"),
            @ApiResponse(responseCode = "400", description = "El proyecto pertenece a otro estudiante"),
            @ApiResponse(responseCode = "404", description = "No existe el proyecto")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<?> deleteProject(@Parameter(description = "Id del proyecto") @PathVariable Integer id, Authentication authentication) {
        projectService.deleteProject(authentication.getName(), id);
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("Proyecto eliminado con exito").build(), HttpStatus.NO_CONTENT);
    }
}
