package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.ProjectDto;
import com.chris.uniconnect.Model.Dto.PublicationDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    PersonDto uploadFileUser(int id , MultipartFile file , String tipoUser);

    ProjectDto uploadFileProject(String username, int id , MultipartFile file );

    PublicationDto uploadFilePublication(String username, int id , MultipartFile file );

    String uploadFilePruebapublication( MultipartFile file);

    //valida la imagen y la sube a la carpeta indicada de Cloudinary, regresa la url
    String uploadImage(MultipartFile file, String folder);
}
