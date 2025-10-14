package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exeptions.Mensaje;
import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Repository.PersonaRepository;
import com.chris.uniconnect.Service.IPersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PersonController {

    private final IPersonService personService;

     @GetMapping("/persons/{id}")
    public ResponseEntity<?> getIdPerson(@PathVariable int id) {

        return new ResponseEntity<>(Mensaje.builder()
                .mensaje("Persona encontrada con exito")
                .object(personService.getPersonsById(id))
                .build(), HttpStatus.OK);
    }

/*
    //Esta ruta igual es para el admnistrador y reclutador
    @PostMapping("/persons")
    public ResponseEntity<?> savePerson(@RequestBody PersonDto person) {
        return null;

    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<?> updatePerson(@RequestBody PersonDto person, @PathVariable int id) {
        PersonDto updatePerson = null;

        if (personService.existPerson(id)) {
            updatePerson = personService.savePersonId(person);
            return new ResponseEntity<>(Mensaje.builder().mensaje("Persona Actualizada con exito").object(updatePerson), HttpStatus.OK);
        }
        return new ResponseEntity<>(Mensaje.builder().mensaje("El id no existe"), HttpStatus.OK);


    }

    //Esta ruta es para el administrador
    @DeleteMapping("/persons")
    public ResponseEntity<?> deletePerson(@RequestBody PersonDto person) {
        PersonDto searchPerson = personService.getPersonId(person.getId());

        if (searchPerson != null) {
            personService.deletePerson(person);
            return new ResponseEntity<>(Mensaje.builder().mensaje("Persona Eliminada con exito"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Mensaje.builder().mensaje("Persona No se encontro con el id " + person.getId()).object(null), HttpStatus.OK);
    }
     */


}
