package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Model.Dto.JobFoundDto;
import com.chris.uniconnect.Model.Entity.JobFound;
import com.chris.uniconnect.Service.IJobFoundService;
import com.chris.uniconnect.payload.MensajeResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class JobFoundController {

    private final IJobFoundService jobFoundService;

    @PostMapping("/jobFound")
    public ResponseEntity<?> createJobFound(@RequestBody JobFoundDto jobFound) {
        try {
            JobFoundDto createJobFoundDto = jobFoundService.createFoundDto(jobFound);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Empleo creado con exito")
                    .object(createJobFoundDto)
                    .build(), HttpStatus.CREATED);
        } catch (DataAccessException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @PutMapping("/jobFound")
    public ResponseEntity<?> updateJobFound(@RequestBody JobFoundDto jobFound) {
        try {
            JobFoundDto createJobFoundDto = jobFoundService.createFoundDto(jobFound);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Empleo actualizado con exito")
                    .object(createJobFoundDto)
                    .build(), HttpStatus.CREATED);
        } catch (DataAccessException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @DeleteMapping("/jobFound")
    public ResponseEntity<?> deleteJobFound(@RequestBody JobFoundDto jobFound) {
        try {
            jobFoundService.deleteFoundDto(jobFound);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("/JobFound eliminado con exito").object(null).build(), HttpStatus.OK);
        } catch (DataAccessException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }
}
