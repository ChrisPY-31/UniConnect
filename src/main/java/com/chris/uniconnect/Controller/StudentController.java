package com.chris.uniconnect.Controller;


import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Model.Dto.StudentAllDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Service.IStudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class StudentController {

    private final IStudentService studentService;

    @GetMapping("/students")
    public ResponseEntity<?> getStudents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(studentService.getStudents(pageable));
    }

    @GetMapping("/students/name")
    public ResponseEntity<?> getStudentsByName(@RequestParam String name) {
        List<StudentAllDto> studentName = studentService.getStudentByName(name);
        if (studentName == null || studentName.isEmpty()) {
            throw new ResourceNotFoundException("students");
        }
        return ResponseEntity.ok(studentName);
    }

    //Este es para el administrador
    @PostMapping("/student")
    public ResponseEntity<?> createStudent(@RequestBody StudentDto studentDto) {
        try {
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("Estudiante creado con exito").object(studentService.createStudent(studentDto)).build(), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDto student, @PathVariable int id) {
        boolean existStudent = studentService.existStudent(id);
        if (existStudent) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Persona Actualizada con exito")
                    .object(studentService.updateStudent(student)).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("La persona con el id: " + id + " no existe").build(), HttpStatus.NOT_FOUND);
    }

}
