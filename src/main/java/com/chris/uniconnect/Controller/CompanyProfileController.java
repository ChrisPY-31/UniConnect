package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.CompanyProfileDto;
import com.chris.uniconnect.Model.Dto.CompanyProfileRequest;
import com.chris.uniconnect.Service.ICompanyProfileService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Empresas", description = "Landing publica de cada empresa con convenio vigente. Nombre y categoria vienen del convenio (los edita el ADMIN); " +
        "descripcion, logo, banner y datos de contacto los edita el COMPANY_ADMIN de esa empresa.")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CompanyProfileController {

    private final ICompanyProfileService companyProfileService;

    @Operation(summary = "Listar empresas", description = "Publico, sin login. Solo convenios vigentes (activos y dentro de fechas), ordenados por nombre.")
    @ApiResponse(responseCode = "200", description = "Lista de empresas (puede ser un array vacio)")
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyProfileDto>> getCompanies() {
        return ResponseEntity.ok(companyProfileService.getPublicProfiles());
    }

    @Operation(summary = "Landing de una empresa", description = "Publico, sin login. Un convenio desactivado o vencido responde 404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "No existe el convenio o no esta vigente")
    })
    @GetMapping("/companies/{idCorporacion}")
    public ResponseEntity<CompanyProfileDto> getCompany(@Parameter(description = "Id del convenio") @PathVariable Long idCorporacion) {
        return ResponseEntity.ok(companyProfileService.getPublicProfile(idCorporacion));
    }

    @Operation(summary = "Perfil publico de mi empresa", description = "Solo COMPANY_ADMIN. La empresa se resuelve por JWT. Se ve aunque el convenio este vencido.")
    @ApiResponse(responseCode = "200", description = "Perfil de la empresa del usuario autenticado")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @GetMapping("/company-admin/me/profile")
    public ResponseEntity<CompanyProfileDto> getMyCompanyProfile(Authentication authentication) {
        return ResponseEntity.ok(companyProfileService.getMyProfile(authentication.getName()));
    }

    @Operation(summary = "Editar el perfil publico de mi empresa",
            description = "Solo COMPANY_ADMIN. Reemplaza descripcion, sitio web, sector, tamano, ubicacion y correo de contacto (un campo vacio o null se borra). " +
                    "Nombre y categoria se editan en el convenio; logo y banner tienen su propio endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "400", description = "URL o correo invalidos, o algun campo demasiado largo")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @PutMapping("/company-admin/me/profile")
    public ResponseEntity<?> updateMyCompanyProfile(@Valid @RequestBody CompanyProfileRequest request, Authentication authentication) {
        CompanyProfileDto profile = companyProfileService.updateMyProfile(authentication.getName(), request);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Perfil de la empresa actualizado con exito")
                .object(profile).build());
    }

    @Operation(summary = "Subir el logo de mi empresa", description = "Solo COMPANY_ADMIN. Imagen jpg, jpeg, png, webp o avif.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logo actualizado"),
            @ApiResponse(responseCode = "400", description = "Archivo vacio o extension no permitida")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @PatchMapping(value = "/company-admin/me/profile/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMyLogo(@Parameter(description = "Archivo de imagen") @RequestParam("image") MultipartFile file,
                                          Authentication authentication) {
        CompanyProfileDto profile = companyProfileService.uploadMyLogo(authentication.getName(), file);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Logo actualizado con exito")
                .object(profile).build());
    }

    @Operation(summary = "Subir el banner de mi empresa", description = "Solo COMPANY_ADMIN. Imagen jpg, jpeg, png, webp o avif.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Banner actualizado"),
            @ApiResponse(responseCode = "400", description = "Archivo vacio o extension no permitida")
    })
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @PatchMapping(value = "/company-admin/me/profile/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMyBanner(@Parameter(description = "Archivo de imagen") @RequestParam("image") MultipartFile file,
                                            Authentication authentication) {
        CompanyProfileDto profile = companyProfileService.uploadMyBanner(authentication.getName(), file);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Banner actualizado con exito")
                .object(profile).build());
    }
}
