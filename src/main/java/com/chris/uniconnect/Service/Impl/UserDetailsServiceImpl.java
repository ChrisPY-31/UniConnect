package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.RecruiterMappers;
import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.Response.AuthCreateUserRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthLoginRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthResponse;
import com.chris.uniconnect.Model.Entity.*;
import com.chris.uniconnect.Repository.*;
import com.chris.uniconnect.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepostory teacherRepostory;

    @Value("${email.private.user}")
    private String emailUser;
    private RecruiterRepository recruiterRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role -> {
                    authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())));
                });

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNonExpired(),
                userEntity.isCredentialsNonExpired(),
                userEntity.isAccountNonLocked(),
                authorityList);
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(username, "User loged successfuly", accessToken, true);
        return authResponse;
    }


    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest, PersonDto person) {
        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();
        String email = authCreateUserRequest.email();

        if (userRepository.existsByEmail(email)) {
            throw new BadCredentialsException("El correo ya esta en uso.");
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailUser);
            mailMessage.setTo(email);
            mailMessage.setSubject("CENTRO UNIVERSITARIO UAEMEX TIANGUISTENCO");
            mailMessage.setText("Hola alumno ya estas registrado en uniConnect tus credenciales de acceso son :\n" +
                    "Usuario: " + username +
                    " Contraseña: " + password + " con tu cuenta ahora podras desarrolar tu perfil profesional.");
            mailSender.send(mailMessage);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();
        Set<RolesEntity> rolesEntitySet = roleRepository.findByRoleEnumIn(roleRequest).stream().collect(Collectors.toSet());

        if (rolesEntitySet.isEmpty()) {
            throw new BadCredentialsException("Los roles especificados no existen");
        }
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .roles(rolesEntitySet)
                .isEnabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();

        UserEntity userCreated = userRepository.save(userEntity);

        if (roleRequest.contains("STUDENT")) {
            Student student = new Student();
            student.setName(person.getNombre());
            student.setLastName(person.getApellido());
            student.setType("student");
            student.setUserEntity(userCreated);
            studentRepository.save(student);
        }
        if (roleRequest.contains("TEACHER")) {
            Teacher teacher = new Teacher();
            teacher.setName(person.getNombre());
            teacher.setLastName(person.getApellido());
            teacher.setUserEntity(userCreated);
            teacher.setType("teacher");
            teacherRepostory.save(teacher);
        }
        if (roleRequest.contains("RECRUITER")) {
            Recruiter recruiter = new Recruiter();
            recruiter.setName(person.getNombre());
            recruiter.setLastName(person.getApellido());
            recruiter.setUserEntity(userCreated);
            recruiter.setType("recruiter");
            recruiterRepository.save(recruiter);
        }

        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();


        userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userCreated.getRoles()
                .stream()
                .flatMap(roles -> roles.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(), userEntity.getPassword(), authorityList);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(userCreated.getUsername(), "User creado con exito", accessToken, true);


        return authResponse;
    }
}
