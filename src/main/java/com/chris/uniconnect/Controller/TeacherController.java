package com.chris.uniconnect.Controller;

import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Service.ITeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class TeacherController {

    private final ITeacherService teacherService;

    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherDto> getTeacher(@PathVariable int id) {
        TeacherDto teacher = teacherService.getTeacherById(id);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/teacher")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDto teacher) {
        TeacherDto saveTeacher = teacherService.createStudent(teacher);

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("cuenta del maestro creada con exito")
                .object(saveTeacher)
                .build(), HttpStatus.CREATED);
    }

    @PutMapping("/teacher/{id}")
    public ResponseEntity<?> updateTeacher(@RequestBody TeacherDto teacher, @PathVariable int id) {

        if (teacherService.existStudent(id)) {
            TeacherDto updateTeacher = teacherService.updateStudent(teacher);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Persona Actualizada con exito")
                    .object(updateTeacher).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("id:" + " no valido").build(), HttpStatus.NOT_FOUND);
    }
}
