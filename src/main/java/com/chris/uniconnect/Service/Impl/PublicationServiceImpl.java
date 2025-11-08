package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.PublicationMappers;
import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.PublicationDto;
import com.chris.uniconnect.Model.Dto.Response.PersonaResponseM;
import com.chris.uniconnect.Model.Entity.Person;
import com.chris.uniconnect.Repository.PersonaRepository;
import com.chris.uniconnect.Repository.PublicationRepository;
import com.chris.uniconnect.Service.FileUploadService;
import com.chris.uniconnect.Service.IPersonService;
import com.chris.uniconnect.Service.IPublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationServiceImpl implements IPublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private IPersonService personService;


    @Override
    public List<PublicationDto> getAllPublications() {
        return PublicationMappers.INSTANCE.publicationListToPublicacionDtoList(publicationRepository.findAll());
    }

    @Override
    public PublicationDto createPublication(PublicationDto publicationDto, MultipartFile file) {
        PersonDto person = personService.getPersonsById(publicationDto.getIdPersona());

        if (publicationDto.getPersona() == null) {
            PersonaResponseM personaResponse = new PersonaResponseM();
            personaResponse.setId(person.getId());
            personaResponse.setNombre(person.getNombre());
            personaResponse.setApellido(person.getApellido());
            personaResponse.setImagen(person.getImagen());
            personaResponse.setEspecialidad(person.getEspecialidad());
        }
        if (file == null) {

            return PublicationMappers.INSTANCE.publicationDtoToPublicacionDto(publicationRepository.save(PublicationMappers.INSTANCE.publicacionDtoToPublication(publicationDto)));
        }
        String imageReady = fileUploadService.uploadFilePruebapublication(file);
        publicationDto.setImagen(imageReady);
        return PublicationMappers.INSTANCE.publicationDtoToPublicacionDto(publicationRepository.save(PublicationMappers.INSTANCE.publicacionDtoToPublication(publicationDto)));

    }

    @Override
    public void deletePublication(PublicationDto publicationDto) {
        publicationRepository.deleteById(publicationDto.getId());
    }

    @Override
    public Boolean existsPublication(Integer idPublication) {
        return publicationRepository.existsById(idPublication);
    }

    @Override
    public PublicationDto publicationById(Integer idPublication) {
        return PublicationMappers.INSTANCE.publicationDtoToPublicacionDto(publicationRepository.findById(idPublication).orElse(null));
    }


}
