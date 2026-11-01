package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.PublicationDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    //foto de perfil del usuario autenticado (cualquier subtipo de Person)
    PersonDto uploadMyProfileImage(String username, MultipartFile file);

    //CV en PDF del estudiante autenticado; las postulaciones guardan una copia de la url
    PersonDto uploadMyResume(String username, MultipartFile file);

    ProjectDto uploadFileProject(String username, int id , MultipartFile file );

    PublicationDto uploadFilePublication(String username, int id , MultipartFile file );

    String uploadFilePruebapublication( MultipartFile file);

    //valida la imagen y la sube a la carpeta indicada de Cloudinary, regresa la url
    String uploadImage(MultipartFile file, String folder);
}
