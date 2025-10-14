package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exeptions.Mensaje;
import com.chris.uniconnect.Model.Dto.PersonContactDto;
import com.chris.uniconnect.Service.IPersonContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class PersonContactController {

    private IPersonContactService personContactService;


    @PostMapping("/saveContact")
    public ResponseEntity<?> createContacto(@RequestBody PersonContactDto personContactDto) {
        return new ResponseEntity<>(Mensaje.builder().mensaje("Contacto registrado con exito").object(personContactService.createPersonContact(personContactDto)).build(), HttpStatus.CREATED);
    }
}
