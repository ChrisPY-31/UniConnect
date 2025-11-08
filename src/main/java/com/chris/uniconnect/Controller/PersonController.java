package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Service.IPersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PersonController {

    private final IPersonService personService;

    @GetMapping("/person/{id}")
    public ResponseEntity<?> getIdPerson(@PathVariable int id) {

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Persona encontrada con exito")
                .object(personService.getPersonsById(id))
                .build(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'TEACHER' , 'STUDENT', 'RECRUITER')")
    @GetMapping("/userAccount/{username}")
    public ResponseEntity<?> getAccountUser(@PathVariable String username) {
        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("Cuenta encontrada correctamente")
                .object(personService.getPersonByUserName(username))
                .build(), HttpStatus.OK);
    }
/*

    //Esta ruta es para el administrador
    @DeleteMapping("/persons")
    public ResponseEntity<?> deletePerson(@RequestBody PersonDto person) {
        PersonDto searchPerson = personService.getPersonId(person.getId());

        if (searchPerson != null) {
            personService.deletePerson(person);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("Persona Eliminada con exito"), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("Persona No se encontro con el id " + person.getId()).object(null), HttpStatus.OK);
    }
     */


}
