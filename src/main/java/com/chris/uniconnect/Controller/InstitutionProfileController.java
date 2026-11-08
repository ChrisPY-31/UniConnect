package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.InstitutionProfileDto;
import com.chris.uniconnect.Model.Dto.InstitutionProfileRequest;
import com.chris.uniconnect.Service.IInstitutionProfileService;
import com.chris.uniconnect.payload.MensajeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Institucion", description = "Pagina publica de la institucion. Es un solo perfil compartido por todos los ADMIN; " +
        "en el feed las publicaciones de cualquier ADMIN aparecen con este nombre y logo.")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class InstitutionProfileController {

    private final IInstitutionProfileService institutionProfileService;

    @Operation(summary = "Pagina de la institucion", description = "Publico, sin login.")
    @ApiResponse(responseCode = "200", description = "Perfil de la institucion")
    @GetMapping("/institution")
    public ResponseEntity<InstitutionProfileDto> getInstitution() {
        return ResponseEntity.ok(institutionProfileService.getProfile());
    }

    @Operation(summary = "Editar la pagina de la institucion",
            description = "Solo ADMIN. Reemplaza nombre (obligatorio), descripcion, sitio web, ubicacion y correo de contacto (un campo opcional vacio o null se borra). " +
                    "Logo y banner tienen su propio endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "400", description = "Nombre vacio, URL o correo invalidos, o algun campo demasiado largo")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/institution")
    public ResponseEntity<?> updateInstitution(@Valid @RequestBody InstitutionProfileRequest request) {
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Perfil de la institucion actualizado con exito")
                .object(institutionProfileService.updateProfile(request)).build());
    }

    @Operation(summary = "Subir el logo de la institucion", description = "Solo ADMIN. Imagen jpg, jpeg, png, webp o avif. Es la foto con la que aparece el admin en el feed.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logo actualizado"),
            @ApiResponse(responseCode = "400", description = "Archivo vacio o extension no permitida")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/institution/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadLogo(@Parameter(description = "Archivo de imagen") @RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Logo actualizado con exito")
                .object(institutionProfileService.uploadLogo(file)).build());
    }

    @Operation(summary = "Subir el banner de la institucion", description = "Solo ADMIN. Imagen jpg, jpeg, png, webp o avif.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Banner actualizado"),
            @ApiResponse(responseCode = "400", description = "Archivo vacio o extension no permitida")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/institution/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadBanner(@Parameter(description = "Archivo de imagen") @RequestParam("image") MultipartFile file) {
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Banner actualizado con exito")
                .object(institutionProfileService.uploadBanner(file)).build());
    }
}
