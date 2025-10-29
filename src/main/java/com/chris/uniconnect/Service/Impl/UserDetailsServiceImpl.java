package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Model.Dto.Response.AuthCreateUserRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthLoginRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthResponse;
import com.chris.uniconnect.Model.Entity.RolesEntity;
import com.chris.uniconnect.Model.Entity.UserEntity;
import com.chris.uniconnect.Repository.RoleRepository;
import com.chris.uniconnect.Repository.UserRepository;
import com.chris.uniconnect.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Value("${email.private.user}")
    private String emailUser;

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

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
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
