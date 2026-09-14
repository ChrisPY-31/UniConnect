package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Service.IPersonService;
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

import java.util.Set;

@Tag(name = "Personas", description = "Vista generica de Person, comun a Student/Teacher/Recruiter (perfil, cuenta, aptitudes elegidas).")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PersonController {

    private final IPersonService personService;

    @Operation(summary = "Obtener el perfil publico de una persona por id", description = "Funciona para Student, Teacher o Recruiter indistintamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe una persona con ese id")
    })
    @GetMapping("/person/{id}")
    public ResponseEntity<?> getIdPerson(@Parameter(description = "Id de la persona (Student/Teacher/Recruiter)") @PathVariable int id) {

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Persona encontrada con exito")
                .object(personService.getPersonsById(id))
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener el perfil por nombre de usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con ese username")
    })
    @PreAuthorize("hasAnyRole('ADMIN' , 'TEACHER' , 'STUDENT', 'RECRUITER')")
    @GetMapping("/userAccount/{username}")
    public ResponseEntity<?> getAccountUser(@Parameter(description = "Username de la cuenta") @PathVariable String username) {
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Cuenta encontrada correctamente")
                .object(personService.getPersonByUserName(username))
                .build(), HttpStatus.OK);
    }


    @Operation(
            summary = "Elegir mis aptitudes",
            description = "Reemplaza el conjunto completo de aptitudes del usuario autenticado (maximo 5, ids del catalogo GET /aptitude). No se usa un idPersona del body: el dueno se resuelve por JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aptitudes actualizadas"),
            @ApiResponse(responseCode = "400", description = "Lista vacia, mas de 5 ids, o algun id no existe en el catalogo")
    })
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'RECRUITER')")
    @PutMapping("/person/aptitudes")
    public ResponseEntity<?> updateAptitudes(@RequestBody Set<Integer> aptitudeIds, Authentication authentication) {
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Aptitudes actualizadas con exito")
                .object(personService.updateAptitudes(authentication.getName(), aptitudeIds))
                .build(), HttpStatus.OK);
    }

    @Operation(summary = "Desactivar una cuenta", description = "Solo ADMIN.")
    @ApiResponse(responseCode = "200", description = "Desactivada, o mensaje de que no existe (ver body)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deletePerson(@Parameter(description = "Id de la persona") @PathVariable Integer id) {
        PersonDto searchPerson = personService.getPersonsById(id);

        if (searchPerson != null) {
            personService.deletePerson(searchPerson);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("Cuenta desactivada con exito").build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("Persona No se encontro con el id " + id).object(null).build(), HttpStatus.OK);
    }


}
