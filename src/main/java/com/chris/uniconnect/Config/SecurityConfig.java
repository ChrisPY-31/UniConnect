package com.chris.uniconnect.Config;

import com.chris.uniconnect.Config.filter.JwtTokenValidator;
import com.chris.uniconnect.Service.Impl.UserDetailsServiceImpl;
import com.chris.uniconnect.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //permite trabajar con anotaciones
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // ✅ usa tu clase CorsConfig
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    //configurar endpoints publicos
                    authorize.requestMatchers(HttpMethod.GET, "/api/v1/students").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, "/api/v1/publication").permitAll();
                    authorize.requestMatchers(HttpMethod.GET, "/api/v1/students").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                    //configurar endpoints privados
                    //permisos generales

                    //permisos de los estudiantes
                    authorize.requestMatchers(HttpMethod.PUT, "/api/v1/student/**").hasAnyRole("STUDENT");
                    authorize.requestMatchers(HttpMethod.POST, "/api/v1/projects").hasRole("STUDENT");
                    authorize.requestMatchers(HttpMethod.PUT, "/api/v1/projects").hasRole("STUDENT");
                    authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/projects/**").hasRole("STUDENT");

                    authorize.requestMatchers(HttpMethod.POST, "/api/v1/projectLink").hasRole("STUDENT");
                    authorize.requestMatchers(HttpMethod.PUT, "/api/v1/projectLink/**").hasRole("STUDENT");
                    authorize.requestMatchers(HttpMethod.POST, "/api/v1/technology").hasRole("STUDENT");
                    authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/technology/**").hasRole("STUDENT");

                    //permisos de los maestros
                    authorize.requestMatchers(HttpMethod.POST, "/api/v1/recomendations").hasRole("TEACHER");
                    authorize.requestMatchers(HttpMethod.PUT, "/api/v1/recomendations/**").hasRole("TEACHER");
                    authorize.requestMatchers(HttpMethod.DELETE, "/api/v1/recomendations/**").hasRole("TEACHER");

                    //permisos de admin
                    authorize.requestMatchers(HttpMethod.GET , "/api/v1/manager").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.POST, "api/v1/publication").hasAnyRole("STUDENT" , "TEACHER", "RECRUITER");


                    //permisos de los reclutadores

                    //configurar el resto de endpoints
                    authorize.anyRequest().authenticated();

                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
