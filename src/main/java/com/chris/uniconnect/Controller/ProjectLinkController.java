package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.ProjectLinksDto;
import com.chris.uniconnect.Service.IProjectLinkService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class ProjectLinkController {

    private final IProjectLinkService projectLinkService;

    //validacion si proyecto no existe
    @PostMapping("/projectLink")
    public ResponseEntity<?> saveProjectLink(@RequestBody ProjectLinksDto projectLink) {

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("redes creada con exito")
                .object(projectLinkService.createProjectLinks(projectLink))
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/projectLink/{id}")
    public ResponseEntity<?> updateProjectLink(@RequestBody ProjectLinksDto projectLink, @PathVariable int id) {

        if (projectLinkService.existsProjectLinks(id) && projectLink != null) {

            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("redes creada con exito")
                    .object(projectLinkService.createProjectLinks(projectLink))
                    .build(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Error no se pudo actualizar")
                .object(null)
                .build(), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/projectLink/{id}")
    public ResponseEntity<?> deleteProjectLink(@PathVariable Integer id) {
        ProjectLinksDto projectLink = projectLinkService.getProjectLinkById(id);

        if (projectLink == null) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("El id " + id + " No existe")
                    .object(null)
                    .build(), HttpStatus.NOT_FOUND);
        }
        projectLinkService.deleteProjectLinks(projectLink);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Red eliminado con exito")
                .object(projectLink)
                .build(), HttpStatus.OK);
    }
}
