package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.PersonContactDto;
import com.chris.uniconnect.Service.IPersonContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
public class PersonContactController {

    private IPersonContactService personContactService;


    @PostMapping("/saveContact")
    public ResponseEntity<?> createContacto(@RequestBody List<PersonContactDto> personContactDto) {
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("Contacto registrado con exito").object(personContactService.createPersonContact(personContactDto)).build(), HttpStatus.CREATED);
    }
}
