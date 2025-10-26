package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.AptitudeDto;
import com.chris.uniconnect.Service.IAptitudeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
public class AptitudeController {

    private IAptitudeService aptitudeService;

    @PostMapping("/apititudes")
    public ResponseEntity<?> createAptitude(@RequestBody List<AptitudeDto> aptitude) {
        return ResponseEntity.ok().body(aptitudeService.createAptitud(aptitude));
    }
}
