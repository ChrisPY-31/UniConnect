package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Enum.Roles;
import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.CompanyAdminMappers;
import com.chris.uniconnect.Model.Dto.CompanyAdminDto;
import com.chris.uniconnect.Model.Dto.CompanyAdminProfileRequest;
import com.chris.uniconnect.Model.Dto.CreateCompanyAdminRequest;
import com.chris.uniconnect.Model.Entity.CompanyAdmin;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import com.chris.uniconnect.Model.Entity.RolesEntity;
import com.chris.uniconnect.Model.Entity.UserEntity;
import com.chris.uniconnect.Repository.CompanyAdminRepository;
import com.chris.uniconnect.Repository.CorporatePartnershipsRepository;
import com.chris.uniconnect.Repository.RoleRepository;
import com.chris.uniconnect.Repository.UserRepository;
import com.chris.uniconnect.Service.ICompanyAdminService;
import com.chris.uniconnect.util.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class CompanyAdminServiceImpl implements ICompanyAdminService {

    @Autowired
    private CompanyAdminRepository companyAdminRepository;

    @Autowired
    private CorporatePartnershipsRepository partnershipsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Override
    @Transactional
    public CompanyAdminDto createCompanyAdmin(Long idPartnership, CreateCompanyAdminRequest request) {
        CorporatePartnershipsUAEMex partnership = partnershipsRepository.findById(idPartnership)
                .orElseThrow(() -> new ResourceNotFoundException("Convenio", "idCorporacion", idPartnership));

        if (!Boolean.TRUE.equals(partnership.getActive())) {
            throw new BadRequestException("El convenio esta desactivado, no se le pueden crear cuentas");
        }
        if (companyAdminRepository.existsOtherActiveAccount(idPartnership, 0)) {
            throw new BadRequestException("El convenio ya tiene una cuenta de empresa activa. Desactivala antes de crear otra");
        }

        String username = request.username().trim();
        String email = request.email().trim();
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new BadRequestException("El username ya esta en uso");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("El correo ya esta en uso");
        }

        RolesEntity role = roleRepository.findByRoleEnumIn(List.of(Roles.COMPANY_ADMIN.name())).stream()
                .findFirst()
                .orElseThrow(() -> new BadRequestException("El rol COMPANY_ADMIN no existe en la base de datos"));

        String password = passwordGenerator.generate();

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .roles(new HashSet<>(List.of(role)))
                .isEnabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();
        UserEntity userCreated = userRepository.save(userEntity);

        CompanyAdmin companyAdmin = new CompanyAdmin();
        companyAdmin.setName(request.nombre().trim());
        companyAdmin.setLastName(request.apellido().trim());
        companyAdmin.setPosition(request.puesto());
        companyAdmin.setType("company_admin");
        companyAdmin.setIdCorporatePartnership(idPartnership);
        companyAdmin.setCorporatePartnershipsUAEMex(partnership);
        companyAdmin.setUserEntity(userCreated);
        CompanyAdmin savedCompanyAdmin = companyAdminRepository.save(companyAdmin);

        //la contrasena solo existe en este correo: si no se puede enviar, se revierte la creacion
        try {
            emailService.sendHtmlEmail(email, "CENTRO UNIVERSITARIO UAEMEX TIANGUISTENCO", username, password);
        } catch (Exception e) {
            log.error("No se pudo enviar el correo de bienvenida a {} para la cuenta de empresa {}", email, username, e);
            throw new BadRequestException("No se pudo enviar el correo con la contrasena, la cuenta no fue creada. Verifica el correo e intenta de nuevo");
        }

        return CompanyAdminMappers.INSTANCE.toDto(savedCompanyAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyAdminDto> getCompanyAdmins(Long idPartnership) {
        List<CompanyAdmin> companyAdmins = idPartnership == null
                ? companyAdminRepository.findAll()
                : companyAdminRepository.findByIdCorporatePartnership(idPartnership);
        return CompanyAdminMappers.INSTANCE.toDtoList(companyAdmins);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyAdminDto getCompanyAdminById(Integer idCompanyAdmin) {
        return CompanyAdminMappers.INSTANCE.toDto(findCompanyAdmin(idCompanyAdmin));
    }

    @Override
    @Transactional
    public CompanyAdminDto changeStatus(Integer idCompanyAdmin, boolean active) {
        CompanyAdmin companyAdmin = findCompanyAdmin(idCompanyAdmin);

        if (active && companyAdminRepository.existsOtherActiveAccount(companyAdmin.getIdCorporatePartnership(), idCompanyAdmin)) {
            throw new BadRequestException("El convenio ya tiene otra cuenta de empresa activa. Desactivala antes de reactivar esta");
        }

        UserEntity userEntity = companyAdmin.getUserEntity();
        userEntity.setEnabled(active);
        userRepository.save(userEntity);
        return CompanyAdminMappers.INSTANCE.toDto(companyAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyAdminDto getMyProfile(String username) {
        return CompanyAdminMappers.INSTANCE.toDto(findByUsername(username));
    }

    @Override
    @Transactional
    public CompanyAdminDto updateMyProfile(String username, CompanyAdminProfileRequest request) {
        CompanyAdmin companyAdmin = findByUsername(username);
        companyAdmin.setName(request.nombre().trim());
        companyAdmin.setLastName(request.apellido().trim());
        companyAdmin.setPosition(request.puesto());
        companyAdmin.setDescription(request.descripcion());
        return CompanyAdminMappers.INSTANCE.toDto(companyAdminRepository.save(companyAdmin));
    }

    private CompanyAdmin findCompanyAdmin(Integer idCompanyAdmin) {
        return companyAdminRepository.findById(idCompanyAdmin)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta de empresa", "id", idCompanyAdmin));
    }

    private CompanyAdmin findByUsername(String username) {
        CompanyAdmin companyAdmin = companyAdminRepository.findByUserEntityUsername(username);
        if (companyAdmin == null) {
            throw new ResourceNotFoundException("Cuenta de empresa", "username", username);
        }
        return companyAdmin;
    }
}
