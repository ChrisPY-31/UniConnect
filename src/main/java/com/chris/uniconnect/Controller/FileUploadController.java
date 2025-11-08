package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' , 'RECRUITER')")
    @PatchMapping("/fileUsers/{id}")
    public ResponseEntity<?> uploadFileUsers(@PathVariable Integer id, @RequestParam("image") MultipartFile file, @RequestParam("tipo") String tipoUser) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadFileUser(id, file, tipoUser));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PatchMapping("/fileProjects/{id}")
    public ResponseEntity<?> uploadProjectFileUsers(@PathVariable Integer id, @RequestParam("image") MultipartFile file) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadFileProject(id, file));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT' , 'TEACHER' ,'RECRUITER')")
    @PatchMapping("/filePublication/{id}")
    public ResponseEntity<?> uploadPublicationsFileUsers(@PathVariable Integer id, @RequestParam("image") MultipartFile file) {
        try {
            return ResponseEntity.ok(fileUploadService.uploadFilePublication(id, file));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
