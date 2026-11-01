package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.PersonMappers;
import com.chris.uniconnect.Model.Dto.*;
import com.chris.uniconnect.Model.Entity.Person;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Repository.PersonaRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Service.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private PersonaRepository personaRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private IProjectService projectService;

    @Autowired
    @Lazy
    private IPublicationService publicationService;


    @Override
    @Transactional
    public PersonDto uploadMyProfileImage(String username, MultipartFile file) {
        Person person = personaRepository.findByUserEntityUsername(username);
        if (person == null) {
            throw new ResourceNotFoundException("Persona", "username", username);
        }
        //solo se toca la imagen, sin pasar por los DTO de cada rol
        person.setImage(uploadImage(file, "Usuarios"));
        personaRepository.save(person);
        return personService.getPersonByUserName(username);
    }

    @Override
    @Transactional
    public PersonDto uploadMyResume(String username, MultipartFile file) {
        Student student = studentRepository.findByUserEntityUsername(username);
        if (student == null) {
            throw new ResourceNotFoundException("Estudiante", "username", username);
        }
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo esta vacio");
        }
        String filename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (!filename.endsWith(".pdf") || !"application/pdf".equals(file.getContentType())) {
            throw new BadRequestException("El CV debe ser un archivo PDF");
        }

        try {
            //raw: Cloudinary bloquea por defecto la entrega de PDFs subidos como image
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "Curriculums", "resource_type", "raw", "format", "pdf")
            );
            student.setResumeUrl(result.get("secure_url").toString());
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
        studentRepository.save(student);
        return personService.getPersonByUserName(username);
    }

    @Override
    public ProjectDto uploadFileProject(String username, int idProject, MultipartFile file) {

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
            projectService.updateProject(username, project);

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }

        return project;
    }

    @Override
    public PublicationDto uploadFilePublication(String username, int idPublication, MultipartFile file) {
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");

        PublicationDto publication = publicationService.publicationById(idPublication);

        if (publication == null) {
            throw new ResourceNotFoundException("Publicacion", "id", idPublication);
        }

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
            return publicationService.updatePublication(username, idPublication, publication);

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
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

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp", "avif");

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("El archivo esta vacio");
        }

        String extension = null;

        if (file.getOriginalFilename() != null) {
            String[] splitName = file.getOriginalFilename().split("\\.");
            if (splitName.length > 1) {
                extension = splitName[splitName.length - 1].toLowerCase();
            }
        }

        if (extension == null || !allowedExtensions.contains(extension)) {
            throw new BadRequestException(String.format("Extension %s no permitida. Extensiones permitidas: %s",
                    extension, allowedExtensions));
        }

        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", folder)
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }


}