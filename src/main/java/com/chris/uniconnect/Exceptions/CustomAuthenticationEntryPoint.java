package com.chris.uniconnect.Exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = "Unauthorized";

        if (authException instanceof LockedException) {
            errorMessage = "User is locked";
        } else if (authException instanceof BadCredentialsException) {
            errorMessage = "Invalid username or password";
        } else if (authException instanceof DisabledException) {
            errorMessage = "User is disabled";
        }

        response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
    }
}
