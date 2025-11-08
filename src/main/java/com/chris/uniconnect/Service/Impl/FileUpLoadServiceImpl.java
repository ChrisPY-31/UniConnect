package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.PersonMappers;
import com.chris.uniconnect.Model.Dto.*;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Repository.PersonaRepository;
import com.chris.uniconnect.Service.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class FileUpLoadServiceImpl implements FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private IPersonService personService;

    @Autowired
    private IStudentService studentService;

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private IRecruiterServce recruiterServce;

    @Autowired
    private IProjectService projectService;


    @Override
    public PersonDto uploadFileUser(int id, MultipartFile file, String tipoUser) {

        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");

        PersonDto personaFile = personService.getPersonsById(id);

        // Validación de archivo nulo
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is null or empty");
        }

        String extension = null;

        if (file.getOriginalFilename() != null) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            if (splitName.length > 1) {
                extension = splitName[splitName.length - 1].toLowerCase();
            }
        }

        // Validación corregida de extensiones
        if (extension == null || !allowedExtensions.contains(extension)) {
            throw new RuntimeException(String.format("Extension %s not allowed. Allowed extensions: %s",
                    extension, allowedExtensions));
        }

        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "Usuarios")
            );

            String imageUserUrl = result.get("secure_url").toString();
            personaFile.setImagen(imageUserUrl);


            if (tipoUser.equals("teacher")) {
                TeacherDto teacher = teacherService.getTeacherById(id);
                teacher.setImagen(imageUserUrl);
                teacherService.updateTeacher(teacher);
            }
            if (tipoUser.equals("student")) {
                StudentDto student = studentService.getStudentById(id);
                student.setImagen(personaFile.getImagen());
                studentService.updateStudent(student);
            }
            if (tipoUser.equals("recruiter")) {
                RecruiterDto recruiter = recruiterServce.getRecruiterById(id);
                recruiter.setImagen(imageUserUrl);
                recruiterServce.createRecruiter(recruiter);
            }

            // Aquí deberías guardar la persona actualizada en la base de datos
            // personService.updatePerson(personaFile);
            return personaFile;

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    @Override
    public ProjectDto uploadFileProject(int idProject, MultipartFile file) {

        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");


        ProjectDto project = projectService.getProject(idProject);

        // Validación de archivo nulo
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is null or empty");
        }

        String extension = null;

        if (file.getOriginalFilename() != null) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            if (splitName.length > 1) {
                extension = splitName[splitName.length - 1].toLowerCase();
            }
        }

        // Validación corregida de extensiones
        if (extension == null || !allowedExtensions.contains(extension)) {
            throw new RuntimeException(String.format("Extension %s not allowed. Allowed extensions: %s",
                    extension, allowedExtensions));
        }

        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "Proyectos")
            );

            String imageProjectUrl = result.get("secure_url").toString();
            project.setImagen(imageProjectUrl);
            projectService.updateProject(project);

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }

        return project;
    }

    @Override
    public PublicationDto uploadFilePublication(int idPublication, MultipartFile file) {
        /*
        *
        * List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");


        PublicationDto publication = publicationService.publicationById(idPublication);

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is null or empty");
        }

        String extension = null;

        if (file.getOriginalFilename() != null) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            if (splitName.length > 1) {
                extension = splitName[splitName.length - 1].toLowerCase();
            }
        }

        if (extension == null || !allowedExtensions.contains(extension)) {
            throw new RuntimeException(String.format("Extension %s not allowed. Allowed extensions: %s",
                    extension, allowedExtensions));
        }

        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "Publicaciones")
            );

            String imagePublicationUrl = result.get("secure_url").toString();
            publication.setImagen(imagePublicationUrl);
            //publicationService.createPublication(publication);
            return publication;



        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
*/
        return null;
    }

    @Override
    public String uploadFilePruebapublication(MultipartFile file) {
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");


        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is null or empty");
        }

        String extension = null;

        if (file.getOriginalFilename() != null) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            if (splitName.length > 1) {
                extension = splitName[splitName.length - 1].toLowerCase();
            }
        }

        if (extension == null || !allowedExtensions.contains(extension)) {
            throw new RuntimeException(String.format("Extension %s not allowed. Allowed extensions: %s",
                    extension, allowedExtensions));
        }

        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "Publicaciones")
            );

            String imagePublicationUrl = result.get("secure_url").toString();

            return imagePublicationUrl;


        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }


}