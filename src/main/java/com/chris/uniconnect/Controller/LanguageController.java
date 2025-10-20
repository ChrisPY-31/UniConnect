package com.chris.uniconnect.Controller;


import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.LanguageDto;
import com.chris.uniconnect.Service.ILanguageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
public class LanguageController {

    private final ILanguageService languageService;

    @PostMapping("/language")
    public ResponseEntity<?> createLanguage(@RequestBody LanguageDto languageDto) {
        languageService.createLanguage(languageDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/language/{id}")
    public ResponseEntity<?> updateLanguage(@RequestBody LanguageDto languageDto, @PathVariable Integer id) {

        if (!languageService.languageExists(id)) {
            LanguageDto language = languageService.createLanguage(languageDto);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("El id : " + id + " No existe").object(language).build(), HttpStatus.OK);
        }

        if (languageDto.getIdIdioma() != null && !languageDto.getIdIdioma().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MensajeResponse.builder()
                            .mensaje("ID del path no coincide con el ID del body")
                            .object(null)
                            .build());
        }
        LanguageDto updatedLanguage = languageService.createLanguage(languageDto);
        return ResponseEntity.ok(MensajeResponse.builder()
                .mensaje("Idioma actualizado con éxito")
                .object(updatedLanguage)
                .build());

    }


    @DeleteMapping("/language/{id}")
    public ResponseEntity<?> deleteLanguage(@PathVariable Integer id) {
        boolean existLanguage = languageService.languageExists(id);

        if (existLanguage) {
            LanguageDto language = languageService.getLanguageById(id);
            languageService.deleteLanguage(language);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("Idioma eliminado con exito").object(null).build(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("No existe el idioma").object(null).build(), HttpStatus.NOT_FOUND);
    }
}
