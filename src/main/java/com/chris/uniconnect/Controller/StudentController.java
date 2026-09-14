package com.chris.uniconnect.Controller;


import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Model.Dto.StudentAllDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Service.IStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Estudiantes", description = "Operacion del Estudiante. PENDIENTE: los listados y updateStudent no tienen @PreAuthorize/validacion de dueno.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class StudentController {

    private final IStudentService studentService;


    @Operation(summary = "Listar estudiantes (paginado)", description = "ADVERTENCIA: sin @PreAuthorize, abierto a cualquiera.")
    @ApiResponse(responseCode = "200", description = "Pagina de estudiantes")
    @GetMapping("/students")
    public ResponseEntity<?> getStudents(@Parameter(description = "Numero de pagina, desde 0") @RequestParam(defaultValue = "0") int page,
                                          @Parameter(description = "Tamano de pagina") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(studentService.getStudents(pageable));
    }

    @Operation(summary = "Buscar estudiantes por nombre/carrera/especialidad", description = "ADVERTENCIA: sin @PreAuthorize, abierto a cualquiera.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estudiantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Ningun estudiante coincide")
    })
    @GetMapping("/students/name/carrera/especialidad")
    public ResponseEntity<?> getStudentsByName(
            @Parameter(description = "Nombre a buscar") @RequestParam String name,
            @Parameter(description = "Carrera") @RequestParam(defaultValue = "Ingeniero en Software") String carrera,
            @Parameter(description = "Especialidad") @RequestParam(defaultValue = "") String especialidad
    ) {
        List<StudentAllDto> studentName = studentService.getStudentByName(name);
        if (studentName == null || studentName.isEmpty()) {
            throw new ResourceNotFoundException("students");
        }
        return ResponseEntity.ok(studentName);
    }

    //Este es para el administrador

    @Operation(summary = "Crear un estudiante", description = "Solo ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estudiante creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/student")
    public ResponseEntity<?> createStudent(@RequestBody StudentDto studentDto) {
        try {
            return new ResponseEntity<>(MensajeResponse.builder().mensaje("Estudiante creado con exito").object(studentService.createStudent(studentDto)).build(), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            summary = "Actualizar el perfil de un estudiante",
            description = "ADVERTENCIA: sin @PreAuthorize y sin validar que el {id} sea el del usuario autenticado — cualquiera puede editar el perfil de cualquier estudiante. Pendiente de cerrar."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "404", description = "No existe un estudiante con ese id")
    })
    @PutMapping("/student/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDto student, @Parameter(description = "Id del estudiante") @PathVariable int id ) {
        boolean existStudent = studentService.existStudent(id);
        if (existStudent) {
            student.setId(id);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Persona Actualizada con exito")
                    .object(studentService.updateStudent(student)).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("La persona con el id: " + id + " no existe").build(), HttpStatus.NOT_FOUND);
    }

}
