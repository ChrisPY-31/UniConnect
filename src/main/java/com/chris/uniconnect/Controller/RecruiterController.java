package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Entity.Recruiter;
import com.chris.uniconnect.Service.IRecruiterServce;
import com.chris.uniconnect.payload.MensajeResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterController {

    private final IRecruiterServce recruiterService;

    @PostMapping("/recruiter")
    public ResponseEntity<?> createRecuiteer(@RequestBody RecruiterDto recruiter) {
        try {
            RecruiterDto createRecuiter = recruiterService.createRecruiter(recruiter);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("cuenta creada con exito")
                    .object(createRecuiter)
                    .build(), HttpStatus.CREATED
            );
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/recruiter/{id}")
    public ResponseEntity<?> updateRecuiteer(@RequestBody RecruiterDto recruiter, @PathVariable Integer id) {

        RecruiterDto updateRecruiter = null;
        try {

            if (!recruiterService.existRecruiter(id)) {
                throw new ResourceNotFoundException("recuiter", "id", id);
            }
            updateRecruiter = recruiterService.updateRecruiter(recruiter);
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("peticion actualizada con exito")
                            .object(updateRecruiter)
                            .build(), HttpStatus.OK
            );

        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    @DeleteMapping("/recruiter/{id}")
    public ResponseEntity<?> deleteRecuiter(@PathVariable Integer id) {

        if(!recruiterService.existRecruiter(id)) {
            throw new ResourceNotFoundException("recuiter", "id", id);
        }
        RecruiterDto deleteRecruiter = recruiterService.getRecruiterById(id);

        return  new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Cuenta elimina con exito")
                        .object(deleteRecruiter)
                        .build(), HttpStatus.OK
        );
    }

}
