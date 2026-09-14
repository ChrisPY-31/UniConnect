package com.chris.uniconnect.Controller;


import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.LanguageDto;
import com.chris.uniconnect.Service.ILanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Idiomas", description = "Idiomas y nivel del perfil autenticado. El idPersona se resuelve por JWT, no por el body.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
public class LanguageController {

    private final ILanguageService languageService;

    @Operation(summary = "Listar mis idiomas")
    @ApiResponse(responseCode = "200", description = "Idiomas del usuario autenticado")
    @GetMapping("/language")
    public ResponseEntity<?> getMyLanguages(Authentication authentication) {
        return ResponseEntity.ok(languageService.getMyLanguages(authentication.getName()));
    }

    @Operation(summary = "Agregar uno o mas idiomas a mi perfil")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Idiomas creados"),
            @ApiResponse(responseCode = "400", description = "Nombre o nivel faltante en algun idioma")
    })
    @PostMapping("/language")
    public ResponseEntity<?> createLanguage(@Valid @RequestBody List<LanguageDto> languageDto, Authentication authentication) {
        List<LanguageDto> saved = languageService.createLanguage(authentication.getName(), languageDto);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Idioma registrado con exito")
                .object(saved)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un idioma propio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o el idioma pertenece a otra persona"),
            @ApiResponse(responseCode = "404", description = "No existe un idioma con ese id")
    })
    @PutMapping("/language/{id}")
    public ResponseEntity<?> updateLanguage(@Parameter(description = "Id del idioma") @PathVariable Integer id,
                                             @Valid @RequestBody LanguageDto languageDto,
                                             Authentication authentication) {
        LanguageDto updated = languageService.updateLanguage(authentication.getName(), id, languageDto);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Idioma actualizado con exito")
                .object(updated)
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un idioma propio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado"),
            @ApiResponse(responseCode = "400", description = "El idioma pertenece a otra persona"),
            @ApiResponse(responseCode = "404", description = "No existe un idioma con ese id")
    })
    @DeleteMapping("/language/{id}")
    public ResponseEntity<?> deleteLanguage(@Parameter(description = "Id del idioma") @PathVariable Integer id,
                                             Authentication authentication) {
        languageService.deleteLanguage(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
