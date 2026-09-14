package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.Response.TeacherDetailResponse;
import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Service.ITeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Profesores", description = "Perfil de Teacher. PENDIENTE: updateTeacher no valida que el {id} sea el del profesor autenticado.")
@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class TeacherController {

    private final ITeacherService teacherService;

    @Operation(summary = "Listar profesores")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de profesores"),
            @ApiResponse(responseCode = "404", description = "No hay profesores registrados")
    })
    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers() {
        List<TeacherDetailResponse> teachers = teacherService.getAllTeachers();
        if (teachers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @Operation(summary = "Crear un profesor", description = "Solo ADMIN.")
    @ApiResponse(responseCode = "201", description = "Profesor creado")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teacher")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDto teacher) {
        TeacherDto saveTeacher = teacherService.createTeacher(teacher);

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("cuenta del maestro creada con exito")
                .object(saveTeacher)
                .build(), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Actualizar el perfil de un profesor",
            description = "ADVERTENCIA: cualquier usuario con rol TEACHER puede editar el perfil de OTRO profesor — el {id} del path no se compara contra el usuario autenticado. Pendiente de cerrar."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "404", description = "No existe un profesor con ese id")
    })
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/teacher/{id}")
    public ResponseEntity<?> updateTeacher(@RequestBody TeacherDto teacher, @Parameter(description = "Id del profesor") @PathVariable int id) {

        if (teacherService.existStudent(id)) {
            teacher.setId(id);
            TeacherDto updateTeacher = teacherService.updateTeacher(teacher);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Persona Actualizada con exito")
                    .object(updateTeacher).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("id:" + " no valido").build(), HttpStatus.NOT_FOUND);
    }
}
