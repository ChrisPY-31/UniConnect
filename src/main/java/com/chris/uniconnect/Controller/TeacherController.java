package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Dto.Response.TeacherDetailResponse;
import com.chris.uniconnect.payload.MensajeResponse;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.chris.uniconnect.Service.ITeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class TeacherController {

    private final ITeacherService teacherService;

    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers() {
        List<TeacherDetailResponse> teachers = teacherService.getAllTeachers();
        if (teachers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teacher")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDto teacher) {
        TeacherDto saveTeacher = teacherService.createTeacher(teacher);

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("cuenta del maestro creada con exito")
                .object(saveTeacher)
                .build(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/teacher/{id}")
    public ResponseEntity<?> updateTeacher(@RequestBody TeacherDto teacher, @PathVariable int id) {

        if (teacherService.existStudent(id)) {
            TeacherDto updateTeacher = teacherService.updateTeacher(teacher);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Persona Actualizada con exito")
                    .object(updateTeacher).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(MensajeResponse.builder().mensaje("id:" + " no valido").build(), HttpStatus.NOT_FOUND);
    }
}
