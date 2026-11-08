package com.chris.uniconnect.Config;

import com.chris.uniconnect.Enum.Roles;
import com.chris.uniconnect.Model.Entity.Administrator;
import com.chris.uniconnect.Model.Entity.InstitutionProfile;
import com.chris.uniconnect.Model.Entity.UserEntity;
import com.chris.uniconnect.Repository.AdministratorRepository;
import com.chris.uniconnect.Repository.UserRepository;
import com.chris.uniconnect.Service.IInstitutionProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//corre en todos los perfiles (tambien prod): asegura el perfil compartido de la institucion
//y que cada usuario ADMIN tenga su Person ligada a el para publicar e interactuar.
//va despues del DemoDataSeeder para alcanzar al admin demo
@Slf4j
@Component
@Order(2)
public class AdminPersonInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final IInstitutionProfileService institutionProfileService;

    public AdminPersonInitializer(UserRepository userRepository,
                                  AdministratorRepository administratorRepository,
                                  IInstitutionProfileService institutionProfileService) {
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.institutionProfileService = institutionProfileService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        InstitutionProfile institution = institutionProfileService.findOrCreate();

        for (UserEntity user : userRepository.findWithoutPersonByRole(Roles.ADMIN)) {
            Administrator administrator = new Administrator();
            administrator.setName("Administracion");
            administrator.setLastName("UniConnect");
            administrator.setType("admin");
            administrator.setUserEntity(user);
            administrator.setInstitutionProfile(institution);
            administratorRepository.save(administrator);
            log.info("Perfil de administrador creado para el usuario {}", user.getUsername());
        }

        for (Administrator administrator : administratorRepository.findByInstitutionProfileIsNull()) {
            administrator.setInstitutionProfile(institution);
            administratorRepository.save(administrator);
        }
    }
}
