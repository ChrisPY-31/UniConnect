package com.chris.uniconnect.Config;

import com.chris.uniconnect.Enum.Roles;
import com.chris.uniconnect.Model.Entity.RolesEntity;
import com.chris.uniconnect.Model.Entity.Student;
import com.chris.uniconnect.Model.Entity.Teacher;
import com.chris.uniconnect.Model.Entity.Technology;
import com.chris.uniconnect.Model.Entity.UserEntity;
import com.chris.uniconnect.Repository.RoleRepository;
import com.chris.uniconnect.Repository.StudentRepository;
import com.chris.uniconnect.Repository.TeacherRepostory;
import com.chris.uniconnect.Repository.TechnologyRepository;
import com.chris.uniconnect.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Profile("dev")
public class DemoDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepostory teacherRepostory;
    private final TechnologyRepository technologyRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(UserRepository userRepository, RoleRepository roleRepository,
                           StudentRepository studentRepository, TeacherRepostory teacherRepostory,
                           TechnologyRepository technologyRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.teacherRepostory = teacherRepostory;
        this.technologyRepository = technologyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        RolesEntity adminRole = ensureRole(Roles.ADMIN);
        RolesEntity teacherRole = ensureRole(Roles.TEACHER);
        RolesEntity studentRole = ensureRole(Roles.STUDENT);
        ensureRole(Roles.RECRUITER);

        seedAdmin(adminRole);
        seedTeacher(teacherRole);
        seedStudent(studentRole);
        seedTechnologies();
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
}
