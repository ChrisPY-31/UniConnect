package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Model.Dto.InstitutionProfileDto;
import com.chris.uniconnect.Model.Dto.InstitutionProfileRequest;
import com.chris.uniconnect.Model.Entity.InstitutionProfile;
import com.chris.uniconnect.Repository.InstitutionProfileRepository;
import com.chris.uniconnect.Service.FileUploadService;
import com.chris.uniconnect.Service.IInstitutionProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class InstitutionProfileServiceImpl implements IInstitutionProfileService {

    @Autowired
    private InstitutionProfileRepository institutionProfileRepository;

    @Autowired
    private FileUploadService fileUploadService;

    //la fila se crea al arrancar (AdminPersonInitializer); esto es solo por si alguien la borra
    @Override
    @Transactional
    public InstitutionProfile findOrCreate() {
        return institutionProfileRepository.findFirstByOrderByIdInstitutionProfileAsc()
                .orElseGet(() -> {
                    InstitutionProfile profile = new InstitutionProfile();
                    profile.setName(InstitutionProfile.DEFAULT_NAME);
                    profile.setUpdatedAt(LocalDateTime.now());
                    return institutionProfileRepository.save(profile);
                });
    }

    //publico: no escribe, si no hay fila responde solo con el nombre por defecto
    @Override
    @Transactional(readOnly = true)
    public InstitutionProfileDto getProfile() {
        return institutionProfileRepository.findFirstByOrderByIdInstitutionProfileAsc()
                .map(this::toDto)
                .orElseGet(() -> new InstitutionProfileDto(InstitutionProfile.DEFAULT_NAME, null, null, null, null, null, null, null));
    }

    @Override
    @Transactional
    public InstitutionProfileDto updateProfile(InstitutionProfileRequest request) {
        InstitutionProfile profile = findOrCreate();
        profile.setName(request.nombre().trim());
        profile.setDescription(blankToNull(request.descripcion()));
        profile.setWebsite(blankToNull(request.sitioWeb()));
        profile.setLocation(blankToNull(request.ubicacion()));
        profile.setContactEmail(blankToNull(request.correoContacto()));
        profile.setUpdatedAt(LocalDateTime.now());
        return toDto(institutionProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public InstitutionProfileDto uploadLogo(MultipartFile file) {
        InstitutionProfile profile = findOrCreate();
        profile.setLogoUrl(fileUploadService.uploadImage(file, "Institucion"));
        profile.setUpdatedAt(LocalDateTime.now());
        return toDto(institutionProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public InstitutionProfileDto uploadBanner(MultipartFile file) {
        InstitutionProfile profile = findOrCreate();
        profile.setBannerUrl(fileUploadService.uploadImage(file, "Institucion"));
        profile.setUpdatedAt(LocalDateTime.now());
        return toDto(institutionProfileRepository.save(profile));
    }

    private InstitutionProfileDto toDto(InstitutionProfile profile) {
        return new InstitutionProfileDto(
                profile.getName(),
                profile.getDescription(),
                profile.getLogoUrl(),
                profile.getBannerUrl(),
                profile.getWebsite(),
                profile.getLocation(),
                profile.getContactEmail(),
                profile.getUpdatedAt()
        );
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
