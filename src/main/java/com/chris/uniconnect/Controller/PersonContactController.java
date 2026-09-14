package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.PersonContactDto;
import com.chris.uniconnect.Service.IPersonContactService;
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

@Tag(name = "Redes de contacto", description = "Redes sociales/contacto (LinkedIn, email, etc.) del perfil autenticado. El idPerson se resuelve por JWT, no por el body.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
public class PersonContactController {

    private IPersonContactService personContactService;

    @Operation(summary = "Listar mis redes de contacto")
    @ApiResponse(responseCode = "200", description = "Redes del usuario autenticado")
    @GetMapping("/personContact")
    public ResponseEntity<?> getMyContacts(Authentication authentication) {
        return ResponseEntity.ok(personContactService.getMyContacts(authentication.getName()));
    }

    @Operation(summary = "Agregar una o mas redes a mi perfil", description = "Cada elemento debe traer un idContact valido (ver GET /contact).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Redes creadas"),
            @ApiResponse(responseCode = "400", description = "Falta el idContact en algun elemento"),
            @ApiResponse(responseCode = "404", description = "El idContact no existe en el catalogo")
    })
    @PostMapping("/personContact")
    public ResponseEntity<?> createContacto(@RequestBody List<PersonContactDto> personContactDto, Authentication authentication) {
        List<PersonContactDto> saved = personContactService.createPersonContact(authentication.getName(), personContactDto);
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Contacto registrado con exito")
                .object(saved)
                .build(), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar una red de mi perfil")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminada"),
            @ApiResponse(responseCode = "404", description = "No tenes esa red registrada")
    })
    @DeleteMapping("/personContact/{idContact}")
    public ResponseEntity<?> deleteContacto(@Parameter(description = "Id del tipo de contacto (catalogo Contact)") @PathVariable Integer idContact,
                                             Authentication authentication) {
        personContactService.deletePersonContact(authentication.getName(), idContact);
        return ResponseEntity.noContent().build();
    }
}
