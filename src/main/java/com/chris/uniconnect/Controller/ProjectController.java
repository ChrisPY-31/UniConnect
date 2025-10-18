package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Service.IProjectService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class ProjectController {

    private final IProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<?> getProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        if (!projects.isEmpty()) {
            return new ResponseEntity<>(MensajeResponse.builder().object(projects).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().object(projects).build(), HttpStatus.NOT_FOUND);
    }

    //verificar la validacion
    @PostMapping("/projects")
    public ResponseEntity<?> saveProject(@RequestBody ProjectDto projectDto) {
        ProjectDto project = null;
        try {
            project = projectService.createProject(projectDto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Proyecto guardado correctamente")
                    .object(project)
                    .build(), HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(MensajeResponse.builder().mensaje(e.getMessage()).object(projectDto).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/projects")
    public ResponseEntity<?> updateProject(@RequestBody ProjectDto projectDto) {
        ProjectDto project = null;

        try {
            Boolean exist = projectService.existBoolean(projectDto.getIdProject());
            if (exist) {
                project = projectService.updateProject(projectDto);
                return new ResponseEntity<>(MensajeResponse.builder().mensaje("Proyecto actualizado correctamente").object(project).build(), HttpStatus.OK);
            }
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("El id: " + projectDto.getIdProject() + "del proyecto en la base de datos")
                    .build(), HttpStatus.NOT_FOUND);

        } catch (DataAccessException e) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(e.getMessage())
                    .object(projectDto)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer id) {
        ProjectDto project = projectService.getProject(id);

        if (project != null) {
            projectService.deleteProject(project);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("proyecto elimnado con exito").build(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("El id: " + id + "del proyecto en la base de datos")
                .build(), HttpStatus.NOT_FOUND);

    }
}
