package com.chris.uniconnect.Config;

import com.chris.uniconnect.Enum.ContactType;
import com.chris.uniconnect.Enum.Roles;
import com.chris.uniconnect.Model.Entity.Aptitude;
import com.chris.uniconnect.Model.Entity.Career;
import com.chris.uniconnect.Model.Entity.CompanyAdmin;
import com.chris.uniconnect.Model.Entity.CompanyProfile;
import com.chris.uniconnect.Model.Entity.Contact;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import com.chris.uniconnect.Model.Entity.Recruiter;
import com.chris.uniconnect.Model.Entity.RolesEntity;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Model.Entity.Technology;
import com.chris.uniconnect.Model.Entity.Ubication;
import com.chris.uniconnect.Model.Entity.UserEntity;
import com.chris.uniconnect.Repository.AptitudeRepository;
import com.chris.uniconnect.Repository.CareerRepository;
import com.chris.uniconnect.Repository.CompanyAdminRepository;
import com.chris.uniconnect.Repository.CompanyProfileRepository;
import com.chris.uniconnect.Repository.ContactRepository;
import com.chris.uniconnect.Repository.CorporatePartnershipsRepository;
import com.chris.uniconnect.Repository.RecruiterRepository;
import com.chris.uniconnect.Repository.RoleRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Repository.TeacherRepostory;
import com.chris.uniconnect.Repository.TechnologyRepository;
import com.chris.uniconnect.Repository.UbicationRepository;
import com.chris.uniconnect.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Profile("dev")
@Order(1)
public class DemoDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepostory teacherRepostory;
    private final TechnologyRepository technologyRepository;
    private final AptitudeRepository aptitudeRepository;
    private final UbicationRepository ubicationRepository;
    private final ContactRepository contactRepository;
    private final CareerRepository careerRepository;
    private final CorporatePartnershipsRepository corporatePartnershipsRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final CompanyAdminRepository companyAdminRepository;
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public DemoDataSeeder(UserRepository userRepository, RoleRepository roleRepository,
                           StudentRepository studentRepository, TeacherRepostory teacherRepostory,
                           TechnologyRepository technologyRepository,
                           AptitudeRepository aptitudeRepository,
                           UbicationRepository ubicationRepository,
                           ContactRepository contactRepository,
                           CareerRepository careerRepository,
                           CorporatePartnershipsRepository corporatePartnershipsRepository,
                           CompanyProfileRepository companyProfileRepository,
                           CompanyAdminRepository companyAdminRepository,
                           RecruiterRepository recruiterRepository,
                           PasswordEncoder passwordEncoder,
                           JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.teacherRepostory = teacherRepostory;
        this.technologyRepository = technologyRepository;
        this.aptitudeRepository = aptitudeRepository;
        this.ubicationRepository = ubicationRepository;
        this.contactRepository = contactRepository;
        this.careerRepository = careerRepository;
        this.corporatePartnershipsRepository = corporatePartnershipsRepository;
        this.companyProfileRepository = companyProfileRepository;
        this.companyAdminRepository = companyAdminRepository;
        this.recruiterRepository = recruiterRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) {
        cleanupLegacyAptitudeSchema();
        cleanupLegacyUbicationSchema();
        ensureCatalogUniqueIndexes();
        dropEnumChecks();

        RolesEntity adminRole = ensureRole(Roles.ADMIN);
        RolesEntity teacherRole = ensureRole(Roles.TEACHER);
        RolesEntity studentRole = ensureRole(Roles.STUDENT);
        RolesEntity recruiterRole = ensureRole(Roles.RECRUITER);
        RolesEntity companyAdminRole = ensureRole(Roles.COMPANY_ADMIN);

        seedAdmin(adminRole);
        seedTeacher(teacherRole);
        seedStudent(studentRole);
        CorporatePartnershipsUAEMex demoPartnership = seedPartnership();
        seedCompanyAdmin(companyAdminRole, demoPartnership);
        seedRecruiter(recruiterRole, demoPartnership);
        seedTechnologies();
        seedAptitudes();
        seedUbications();
        seedContacts();
        seedCareers();
    }

    private RolesEntity ensureRole(Roles role) {
        List<RolesEntity> found = roleRepository.findByRoleEnumIn(List.of(role.name()));
        if (!found.isEmpty()) {
            return found.get(0);
        }
        RolesEntity created = RolesEntity.builder()
                .roleEnum(role)
                .permissionList(new HashSet<>())
                .build();
        return roleRepository.save(created);
    }

    private void seedAdmin(RolesEntity adminRole) {
        String username = "admin";
        if (userRepository.findUserByUsername(username).isPresent()) {
            return;
        }
        UserEntity admin = UserEntity.builder()
                .username(username)
                .email("admin@uniconnect.com")
                .password(passwordEncoder.encode("Admin123*"))
                .isEnabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(Set.of(adminRole))
                .build();
        userRepository.save(admin);
        log.info("Usuario demo creado -> username: admin | password: Admin123*");
    }

    private void seedTeacher(RolesEntity teacherRole) {
        String username = "profesor.demo";
        if (userRepository.findUserByUsername(username).isPresent()) {
            return;
        }
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .email("profesor.demo@uniconnect.com")
                .password(passwordEncoder.encode("Teacher123*"))
                .isEnabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(Set.of(teacherRole))
                .build();
        UserEntity savedUser = userRepository.save(userEntity);

        Teacher teacher = new Teacher();
        teacher.setName("Profesor");
        teacher.setLastName("Demo");
        teacher.setType("teacher");
        teacher.setUserEntity(savedUser);
        teacherRepostory.save(teacher);
        log.info("Usuario demo creado -> username: profesor.demo | password: Teacher123*");
    }

    //convenio demo vigente (sin fecha final) para probar las cuentas de empresa y reclutador
    private CorporatePartnershipsUAEMex seedPartnership() {
        String companyName = "Empresa Demo";
        CorporatePartnershipsUAEMex partnership = corporatePartnershipsRepository.findByCompanyNameIgnoreCase(companyName)
                .orElseGet(() -> {
                    CorporatePartnershipsUAEMex created = new CorporatePartnershipsUAEMex();
                    created.setCompanyName(companyName);
                    created.setCategory("Tecnologia");
                    created.setAgreementType("Practicas profesionales");
                    created.setStartDate(LocalDate.now().withDayOfYear(1));
                    created.setActive(true);
                    log.info("Convenio demo creado -> {}", companyName);
                    return corporatePartnershipsRepository.save(created);
                });
        Long idPartnership = partnership.getIdCorporatePartnership();
        if (companyProfileRepository.findByIdCorporatePartnership(idPartnership).isEmpty()) {
            CompanyProfile profile = new CompanyProfile();
            profile.setIdCorporatePartnership(idPartnership);
            profile.setDescription("Empresa de prueba para el ambiente de desarrollo.");
            companyProfileRepository.save(profile);
        }
        return partnership;
    }

    private void seedCompanyAdmin(RolesEntity companyAdminRole, CorporatePartnershipsUAEMex partnership) {
        String username = "empresa.demo";
        if (userRepository.findUserByUsername(username).isPresent()) {
            return;
        }
        UserEntity savedUser = userRepository.save(demoUser(username, "Empresa123*", companyAdminRole));

        CompanyAdmin companyAdmin = new CompanyAdmin();
        companyAdmin.setName("Empresa");
        companyAdmin.setLastName("Demo");
        companyAdmin.setPosition("Recursos Humanos");
        companyAdmin.setType("company_admin");
        companyAdmin.setIdCorporatePartnership(partnership.getIdCorporatePartnership());
        companyAdmin.setCorporatePartnershipsUAEMex(partnership);
        companyAdmin.setUserEntity(savedUser);
        companyAdminRepository.save(companyAdmin);
        log.info("Usuario demo creado -> username: empresa.demo | password: Empresa123*");
    }

    private void seedRecruiter(RolesEntity recruiterRole, CorporatePartnershipsUAEMex partnership) {
        String username = "reclutador.demo";
        if (userRepository.findUserByUsername(username).isPresent()) {
            return;
        }
        UserEntity savedUser = userRepository.save(demoUser(username, "Recruiter123*", recruiterRole));

        Recruiter recruiter = new Recruiter();
        recruiter.setName("Reclutador");
        recruiter.setLastName("Demo");
        recruiter.setPosition("Reclutador de talento");
        recruiter.setType("recruiter");
        recruiter.setIdCorporatePartnership(partnership.getIdCorporatePartnership());
        recruiter.setCorporatePartnershipsUAEMex(partnership);
        recruiter.setUserEntity(savedUser);
        recruiterRepository.save(recruiter);
        log.info("Usuario demo creado -> username: reclutador.demo | password: Recruiter123*");
    }

    private UserEntity demoUser(String username, String password, RolesEntity role) {
        return UserEntity.builder()
                .username(username)
                .email(username + "@uniconnect.com")
                .password(passwordEncoder.encode(password))
                .isEnabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(Set.of(role))
                .build();
    }

    private void seedStudent(RolesEntity studentRole) {
        String username = "estudiante.demo";
        if (userRepository.findUserByUsername(username).isPresent()) {
            return;
        }
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .email("estudiante.demo@uniconnect.com")
                .password(passwordEncoder.encode("Student123*"))
                .isEnabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(Set.of(studentRole))
                .build();
        UserEntity savedUser = userRepository.save(userEntity);

        Student student = new Student();
        student.setName("Estudiante");
        student.setLastName("Demo");
        student.setType("student");
        student.setUserEntity(savedUser);
        studentRepository.save(student);
        log.info("Usuario demo creado -> username: estudiante.demo | password: Student123*");
    }

    private void seedTechnologies() {
        List<String> catalogo = List.of(
                "Java", "Python", "JavaScript", "TypeScript", "C++", "C#", "PHP", "Kotlin", "Go",
                "Spring Boot", "React", "Angular", "Vue.js", "Node.js", "Django", "Flask", ".NET",
                "MySQL", "PostgreSQL", "MongoDB", "Docker", "Git"
        );

        for (String nombre : catalogo) {
            if (!technologyRepository.existsByNameIgnoreCase(nombre)) {
                Technology technology = new Technology();
                technology.setName(nombre);
                technologyRepository.save(technology);
            }
        }
    }

    private void cleanupLegacyAptitudeSchema() {
        jdbcTemplate.execute("ALTER TABLE aptitude DROP COLUMN IF EXISTS id_person");
        jdbcTemplate.execute(
                "DELETE FROM aptitude a USING aptitude b " +
                        "WHERE a.id_aptitude > b.id_aptitude AND lower(a.name) = lower(b.name)"
        );
    }

    //Hibernate 6 crea un CHECK con los valores del enum y ddl-auto=update no lo actualiza al agregar valores nuevos
    private void dropEnumChecks() {
        jdbcTemplate.execute("ALTER TABLE roles DROP CONSTRAINT IF EXISTS roles_role_enum_check");
        jdbcTemplate.execute("ALTER TABLE IF EXISTS notification DROP CONSTRAINT IF EXISTS notification_type_check");
        jdbcTemplate.execute("ALTER TABLE IF EXISTS job_application DROP CONSTRAINT IF EXISTS job_application_application_status_check");
    }

    private void ensureCatalogUniqueIndexes() {
        jdbcTemplate.execute("CREATE UNIQUE INDEX IF NOT EXISTS ux_aptitude_name_lower ON aptitude (lower(name))");
        jdbcTemplate.execute("CREATE UNIQUE INDEX IF NOT EXISTS ux_technology_name_lower ON technology (lower(name))");
    }

    private void seedAptitudes() {
        List<String> catalogo = List.of(
                "Liderazgo", "Trabajo en equipo", "Comunicacion", "Resolucion de problemas",
                "Pensamiento critico", "Adaptabilidad", "Creatividad", "Gestion del tiempo",
                "Proactividad", "Atencion al detalle", "Aprendizaje continuo", "Toma de decisiones",
                "Organizacion", "Manejo de conflictos", "Orientacion a resultados", "Negociacion",
                "Empatia", "Autonomia", "Pensamiento analitico", "Etica de trabajo"
        );

        for (String nombre : catalogo) {
            if (!aptitudeRepository.existsByNameIgnoreCase(nombre)) {
                Aptitude aptitude = new Aptitude();
                aptitude.setName(nombre);
                aptitudeRepository.save(aptitude);
            }
        }
    }

    private void cleanupLegacyUbicationSchema() {
        jdbcTemplate.execute("ALTER TABLE ubication DROP COLUMN IF EXISTS country");
        jdbcTemplate.execute("ALTER TABLE ubication DROP COLUMN IF EXISTS city");
        jdbcTemplate.execute(
                "DELETE FROM ubication u WHERE u.state IS NULL " +
                        "AND NOT EXISTS (SELECT 1 FROM person p WHERE p.id_ubication = u.id_ubication)"
        );
    }

    private void seedUbications() {
        List<String> catalogo = List.of(
                "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
                "Chihuahua", "Ciudad de Mexico", "Coahuila", "Colima", "Durango", "Estado de Mexico",
                "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacan", "Morelos", "Nayarit",
                "Nuevo Leon", "Oaxaca", "Puebla", "Queretaro", "Quintana Roo", "San Luis Potosi",
                "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatan", "Zacatecas"
        );

        for (String estado : catalogo) {
            if (!ubicationRepository.existsByStateIgnoreCase(estado)) {
                Ubication ubication = new Ubication();
                ubication.setState(estado);
                ubicationRepository.save(ubication);
            }
        }
    }

    private void seedContacts() {
        for (ContactType type : ContactType.values()) {
            if (!contactRepository.existsByContact(type)) {
                Contact contact = new Contact();
                contact.setContact(type);
                contactRepository.save(contact);
            }
        }
    }

    private void seedCareers() {
        List<String> catalogo = List.of(
                "Ingeniero en Software", "Ingeniero en Computacion", "Ingeniero en Ciberseguridad",
                "Ingeniero en Plasticos", "Ingeniero en Produccion Industrial"
        );

        for (String nombre : catalogo) {
            if (!careerRepository.existsByCareerNameIgnoreCase(nombre)) {
                Career career = new Career();
                career.setCareerName(nombre);
                careerRepository.save(career);
            }
        }
    }
}
