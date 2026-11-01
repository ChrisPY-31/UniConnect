package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Enum.Roles;
import com.chris.uniconnect.Exceptions.BadRequestException;
import com.chris.uniconnect.Exceptions.ResourceNotFoundException;
import com.chris.uniconnect.Mappers.RecruiterMappers;
import com.chris.uniconnect.Model.Dto.CreateRecruiterRequest;
import com.chris.uniconnect.Model.Dto.RecruiterAccountDto;
import com.chris.uniconnect.Model.Entity.CompanyAdmin;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import com.chris.uniconnect.Model.Entity.Recruiter;
import com.chris.uniconnect.Model.Entity.RolesEntity;
import com.chris.uniconnect.Model.Entity.UserEntity;
import com.chris.uniconnect.Repository.CompanyAdminRepository;
import com.chris.uniconnect.Repository.CorporatePartnershipsRepository;
import com.chris.uniconnect.Repository.RecruiterRepository;
import com.chris.uniconnect.Repository.RoleRepository;
import com.chris.uniconnect.Repository.UserRepository;
import com.chris.uniconnect.Service.IRecruiterAccountService;
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
public class RecruiterAccountServiceImpl implements IRecruiterAccountService {

    private static final String EMAIL_SUBJECT = "CENTRO UNIVERSITARIO UAEMEX TIANGUISTENCO";

    @Autowired
    private RecruiterRepository recruiterRepository;

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
    public RecruiterAccountDto createRecruiter(String companyAdminUsername, CreateRecruiterRequest request) {
        CompanyAdmin companyAdmin = findCompanyAdmin(companyAdminUsername);
        CorporatePartnershipsUAEMex partnership = companyAdmin.getCorporatePartnershipsUAEMex();
        validatePartnershipActive(partnership);

        String username = request.username().trim();
        String email = request.email().trim();
        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new BadRequestException("El username ya esta en uso");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("El correo ya esta en uso");
        }

        RolesEntity role = roleRepository.findByRoleEnumIn(List.of(Roles.RECRUITER.name())).stream()
                .findFirst()
                .orElseThrow(() -> new BadRequestException("El rol RECRUITER no existe en la base de datos"));

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

        Recruiter recruiter = new Recruiter();
        recruiter.setName(request.nombre().trim());
        recruiter.setLastName(request.apellido().trim());
        recruiter.setPosition(request.puesto());
        recruiter.setType("recruiter");
        recruiter.setIdCorporatePartnership(companyAdmin.getIdCorporatePartnership());
        recruiter.setCorporatePartnershipsUAEMex(partnership);
        recruiter.setUserEntity(userCreated);
        Recruiter savedRecruiter = recruiterRepository.save(recruiter);

        //la contrasena solo existe en este correo: si no se puede enviar, se revierte la creacion
        try {
            emailService.sendHtmlEmail(email, EMAIL_SUBJECT, username, password);
        } catch (Exception e) {
            log.error("No se pudo enviar el correo de bienvenida a {} para el reclutador {}", email, username, e);
            throw new BadRequestException("No se pudo enviar el correo con la contrasena, la cuenta no fue creada. Verifica el correo e intenta de nuevo");
        }

        return RecruiterMappers.INSTANCE.toAccountDto(savedRecruiter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterAccountDto> getMyRecruiters(String companyAdminUsername, Boolean activo) {
        Long idPartnership = findCompanyAdmin(companyAdminUsername).getIdCorporatePartnership();
        List<Recruiter> recruiters = activo == null
                ? recruiterRepository.findByIdCorporatePartnership(idPartnership)
                : recruiterRepository.findByIdCorporatePartnershipAndUserEntityIsEnabled(idPartnership, activo);
        return RecruiterMappers.INSTANCE.toAccountDtoList(recruiters);
    }

    @Override
    @Transactional(readOnly = true)
    public RecruiterAccountDto getMyRecruiter(String companyAdminUsername, Integer idRecruiter) {
        return RecruiterMappers.INSTANCE.toAccountDto(findMyRecruiter(companyAdminUsername, idRecruiter));
    }

    @Override
    @Transactional
    public RecruiterAccountDto changeMyRecruiterStatus(String companyAdminUsername, Integer idRecruiter, boolean active) {
        return changeStatus(findMyRecruiter(companyAdminUsername, idRecruiter), active);
    }

    @Override
    @Transactional
    public void resetMyRecruiterPassword(String companyAdminUsername, Integer idRecruiter) {
        Recruiter recruiter = findMyRecruiter(companyAdminUsername, idRecruiter);
        UserEntity userEntity = recruiter.getUserEntity();
        if (!userEntity.isEnabled()) {
            throw new BadRequestException("La cuenta del reclutador esta desactivada, activala antes de restablecer la contrasena");
        }

        String password = passwordGenerator.generate();
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);

        //igual que al crear: si el correo falla, la contrasena anterior se conserva
        try {
            emailService.sendEmailUpdate(userEntity.getEmail(), EMAIL_SUBJECT, buildResetPasswordHtml(userEntity.getUsername(), password));
        } catch (Exception e) {
            log.error("No se pudo enviar el correo de restablecimiento a {} para el reclutador {}", userEntity.getEmail(), userEntity.getUsername(), e);
            throw new BadRequestException("No se pudo enviar el correo con la nueva contrasena, la contrasena no fue cambiada");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruiterAccountDto> getRecruitersByPartnership(Long idPartnership) {
        if (!partnershipsRepository.existsById(idPartnership)) {
            throw new ResourceNotFoundException("Convenio", "idCorporacion", idPartnership);
        }
        return RecruiterMappers.INSTANCE.toAccountDtoList(recruiterRepository.findByIdCorporatePartnership(idPartnership));
    }

    @Override
    @Transactional
    public RecruiterAccountDto changeRecruiterStatus(Integer idRecruiter, boolean active) {
        Recruiter recruiter = recruiterRepository.findById(idRecruiter)
                .orElseThrow(() -> new ResourceNotFoundException("Reclutador", "id", idRecruiter));
        return changeStatus(recruiter, active);
    }

    private RecruiterAccountDto changeStatus(Recruiter recruiter, boolean active) {
        if (active) {
            validatePartnershipActive(recruiter.getCorporatePartnershipsUAEMex());
        }
        UserEntity userEntity = recruiter.getUserEntity();
        userEntity.setEnabled(active);
        userRepository.save(userEntity);
        return RecruiterMappers.INSTANCE.toAccountDto(recruiter);
    }

    private CompanyAdmin findCompanyAdmin(String username) {
        CompanyAdmin companyAdmin = companyAdminRepository.findByUserEntityUsername(username);
        if (companyAdmin == null) {
            throw new ResourceNotFoundException("Cuenta de empresa", "username", username);
        }
        return companyAdmin;
    }

    //404 tambien cuando existe pero es de otro convenio, para no revelar reclutadores ajenos
    private Recruiter findMyRecruiter(String companyAdminUsername, Integer idRecruiter) {
        Long idPartnership = findCompanyAdmin(companyAdminUsername).getIdCorporatePartnership();
        return recruiterRepository.findByIdAndIdCorporatePartnership(idRecruiter, idPartnership)
                .orElseThrow(() -> new ResourceNotFoundException("Reclutador", "id", idRecruiter));
    }

    private void validatePartnershipActive(CorporatePartnershipsUAEMex partnership) {
        if (partnership == null || !Boolean.TRUE.equals(partnership.getActive())) {
            throw new BadRequestException("El convenio esta desactivado, no se pueden crear ni activar reclutadores");
        }
    }

    private String buildResetPasswordHtml(String username, String password) {
        return "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;\">" +
                "<h2 style=\"color: #2c3e50;\">Restablecimiento de contrasena - UniConnect</h2>" +
                "<p>La empresa con la que colaboras restablecio la contrasena de tu cuenta de reclutador.</p>" +
                "<p><strong>Usuario:</strong> " + username + "<br>" +
                "<strong>Nueva contrasena:</strong> " + password + "</p>" +
                "<p>Te recomendamos cambiarla despues de iniciar sesion.</p>" +
                "</div>";
    }
}
