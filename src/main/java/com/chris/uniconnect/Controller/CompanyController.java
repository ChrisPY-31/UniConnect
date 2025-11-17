package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Model.Dto.CompanyDto;
import com.chris.uniconnect.Model.Entity.Company;
import com.chris.uniconnect.Service.ICompanyService;
import com.chris.uniconnect.payload.MensajeResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @GetMapping("/company")
    public ResponseEntity<?> getAllCompanies() {
        List<CompanyDto> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            throw new ResourceNotFoundException("company");
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("Peticion con exito").object(companies).build(), HttpStatus.OK);
    }

    @PostMapping("/compony")
    public ResponseEntity<?> createCompany(@RequestBody CompanyDto companyDto) {
        try {
            CompanyDto createCompany = companyService.createCompany(companyDto);
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("compania creada con exito").object(createCompany).build(), HttpStatus.CREATED);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<?> updateCompany(@RequestBody CompanyDto companyDto, @PathVariable Integer id) {

        CompanyDto updateCompany = null;
        try {
            if (!companyService.existCompany(id)) {
                throw new ResourceNotFoundException("company", "id", id);

            }

            return new ResponseEntity<>(MensajeResponse.builder().mensaje("compania actualizada con exito").object(updateCompany).build(), HttpStatus.OK);
        } catch (DataAccessException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
    //throw new BadRequestException(ex.getMessage());


    @DeleteMapping("/company/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Integer id) {
        CompanyDto deleteCompany = null;

        try {
            if (!companyService.existCompany(id)) {
                throw new ResourceNotFoundException("company", "id", id);
            }
            deleteCompany = companyService.getCompanyById(id);

            return new ResponseEntity<>(MensajeResponse.builder().mensaje("compania eliminada con exito").object(deleteCompany).build(), HttpStatus.OK);
        } catch (DataAccessException ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }
}
