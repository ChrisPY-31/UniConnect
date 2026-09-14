package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Subida de archivos", description = "Imagenes de perfil, proyecto y publicacion (jpg, jpeg, png, webp, avif) via Cloudinary.")
@RestController
@RequestMapping("api/v1")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Operation(
            summary = "Subir foto de perfil",
            description = "ADVERTENCIA: el {id} del path no se valida contra el usuario autenticado — cualquiera con rol STUDENT/TEACHER/RECRUITER puede cambiar la foto de otra persona. Pendiente de cerrar."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen actualizada"),
            @ApiResponse(responseCode = "500", description = "Extension no permitida, archivo vacio o tipo de usuario invalido")
    })
    @PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
    @PatchMapping("/fileUsers/{id}")
    public ResponseEntity<?> uploadFileUsers(@Parameter(description = "Id de la persona") @PathVariable Integer id,
                                              @Parameter(description = "Archivo de imagen") @RequestParam("image") MultipartFile file,
                                              @Parameter(description = "student, teacher o recruiter") @RequestParam("tipo") String tipoUser) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadFileUser(id, file, tipoUser));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Operation(summary = "Subir imagen de un proyecto propio", description = "Solo STUDENT. Se valida que el proyecto sea del usuario autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen actualizada"),
            @ApiResponse(responseCode = "400", description = "El proyecto no es tuyo"),
            @ApiResponse(responseCode = "404", description = "El proyecto no existe"),
            @ApiResponse(responseCode = "500", description = "Extension no permitida o archivo vacio")
    })
    @PreAuthorize("hasAnyRole('STUDENT')")
    @PatchMapping("/fileProjects/{id}")
    public ResponseEntity<?> uploadProjectFileUsers(@Parameter(description = "Id del proyecto") @PathVariable Integer id,
                                                     @Parameter(description = "Archivo de imagen") @RequestParam("image") MultipartFile file,
                                                     Authentication authentication) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadFileProject(authentication.getName(), id, file));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Operation(summary = "Subir imagen de una publicacion propia", description = "Se valida que la publicacion sea del usuario autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen actualizada"),
            @ApiResponse(responseCode = "400", description = "La publicacion no es tuya"),
            @ApiResponse(responseCode = "404", description = "La publicacion no existe"),
            @ApiResponse(responseCode = "500", description = "Extension no permitida o archivo vacio")
    })
    @PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' ,'RECRUITER')")
    @PatchMapping("/filePublication/{id}")
    public ResponseEntity<?> uploadPublicationsFileUsers(@Parameter(description = "Id de la publicacion") @PathVariable Integer id,
                                                          @Parameter(description = "Archivo de imagen") @RequestParam("image") MultipartFile file,
                                                          Authentication authentication) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadFilePublication(authentication.getName(), id, file));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
