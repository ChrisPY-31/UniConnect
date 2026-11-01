package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.CompanyProfileMappers;
import com.chris.uniconnect.Model.Dto.CompanyProfileDto;
import com.chris.uniconnect.Model.Dto.CompanyProfileRequest;
import com.chris.uniconnect.Model.Entity.CompanyAdmin;
import com.chris.uniconnect.Model.Entity.CompanyProfile;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import com.chris.uniconnect.Repository.CompanyAdminRepository;
import com.chris.uniconnect.Repository.CompanyProfileRepository;
import com.chris.uniconnect.Repository.CorporatePartnershipsRepository;
import com.chris.uniconnect.Service.FileUploadService;
import com.chris.uniconnect.Service.ICompanyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CompanyProfileServiceImpl implements ICompanyProfileService {

    @Autowired
    private CompanyProfileRepository companyProfileRepository;

    @Autowired
    private CorporatePartnershipsRepository partnershipsRepository;

    @Autowired
    private CompanyAdminRepository companyAdminRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    @Transactional
    public void createEmptyProfile(Long idPartnership) {
        findOrCreateProfile(idPartnership);
    }

    //solo convenios vigentes; un convenio sin perfil todavia sale con nombre y categoria
    @Override
    @Transactional(readOnly = true)
    public List<CompanyProfileDto> getPublicProfiles() {
        List<CorporatePartnershipsUAEMex> partnerships = partnershipsRepository.findAll().stream()
                .filter(CorporatePartnershipsUAEMex::isVigente)
                .sorted(Comparator.comparing(CorporatePartnershipsUAEMex::getCompanyName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        Map<Long, CompanyProfile> profiles = companyProfileRepository
                .findByIdCorporatePartnershipIn(partnerships.stream().map(CorporatePartnershipsUAEMex::getIdCorporatePartnership).toList())
                .stream()
                .collect(Collectors.toMap(CompanyProfile::getIdCorporatePartnership, Function.identity()));

        return partnerships.stream()
                .map(partnership -> CompanyProfileMappers.INSTANCE.toDto(partnership, profiles.get(partnership.getIdCorporatePartnership())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyProfileDto getPublicProfile(Long idPartnership) {
        //un convenio desactivado o vencido responde igual que uno inexistente
        CorporatePartnershipsUAEMex partnership = partnershipsRepository.findById(idPartnership)
                .filter(CorporatePartnershipsUAEMex::isVigente)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", "idCorporacion", idPartnership));

        CompanyProfile profile = companyProfileRepository.findByIdCorporatePartnership(idPartnership).orElse(null);
        return CompanyProfileMappers.INSTANCE.toDto(partnership, profile);
    }

    @Override
    @Transactional
    public CompanyProfileDto getMyProfile(String username) {
        CompanyAdmin companyAdmin = findCompanyAdmin(username);
        return CompanyProfileMappers.INSTANCE.toDto(companyAdmin.getCorporatePartnershipsUAEMex(), findOrCreateProfile(companyAdmin));
    }

    @Override
    @Transactional
    public CompanyProfileDto updateMyProfile(String username, CompanyProfileRequest request) {
        CompanyAdmin companyAdmin = findCompanyAdmin(username);
        CompanyProfile profile = findOrCreateProfile(companyAdmin);

        profile.setDescription(blankToNull(request.descripcion()));
        profile.setWebsite(blankToNull(request.sitioWeb()));
        profile.setSector(blankToNull(request.sector()));
        profile.setCompanySize(blankToNull(request.tamano()));
        profile.setLocation(blankToNull(request.ubicacion()));
        profile.setContactEmail(blankToNull(request.correoContacto()));
        profile.setUpdatedAt(LocalDateTime.now());

        return CompanyProfileMappers.INSTANCE.toDto(companyAdmin.getCorporatePartnershipsUAEMex(), companyProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public CompanyProfileDto uploadMyLogo(String username, MultipartFile file) {
        CompanyAdmin companyAdmin = findCompanyAdmin(username);
        CompanyProfile profile = findOrCreateProfile(companyAdmin);

        profile.setLogoUrl(fileUploadService.uploadImage(file, "Empresas"));
        profile.setUpdatedAt(LocalDateTime.now());

        return CompanyProfileMappers.INSTANCE.toDto(companyAdmin.getCorporatePartnershipsUAEMex(), companyProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public CompanyProfileDto uploadMyBanner(String username, MultipartFile file) {
        CompanyAdmin companyAdmin = findCompanyAdmin(username);
        CompanyProfile profile = findOrCreateProfile(companyAdmin);

        profile.setBannerUrl(fileUploadService.uploadImage(file, "Empresas"));
        profile.setUpdatedAt(LocalDateTime.now());

        return CompanyProfileMappers.INSTANCE.toDto(companyAdmin.getCorporatePartnershipsUAEMex(), companyProfileRepository.save(profile));
    }

    //el convenio sale del JWT, nunca del cliente
    private CompanyAdmin findCompanyAdmin(String username) {
        CompanyAdmin companyAdmin = companyAdminRepository.findByUserEntityUsername(username);
        if (companyAdmin == null) {
            throw new BadRequestException("El usuario autenticado no es una cuenta de empresa");
        }
        return companyAdmin;
    }

    //convenios creados antes de esta fase no tienen perfil: se crea la primera vez que la empresa lo usa
    private CompanyProfile findOrCreateProfile(CompanyAdmin companyAdmin) {
        return findOrCreateProfile(companyAdmin.getIdCorporatePartnership());
    }

    private CompanyProfile findOrCreateProfile(Long idPartnership) {
        return companyProfileRepository.findByIdCorporatePartnership(idPartnership)
                .orElseGet(() -> {
                    CompanyProfile profile = new CompanyProfile();
                    profile.setIdCorporatePartnership(idPartnership);
                    profile.setUpdatedAt(LocalDateTime.now());
                    return companyProfileRepository.save(profile);
                });
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
