package com.chris.uniconnect.Model.Dto.Response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//rol: ADMIN, STUDENT, TEACHER, RECRUITER o COMPANY_ADMIN (sin el prefijo ROLE_), para que el front decida a donde redirigir
@JsonPropertyOrder({"username", "rol", "message", "jwt", "status"})
public record AuthResponse(String username,
                           String rol,
                           String message,
                           String jwt,
                           boolean status,
                           boolean accoundLocked
                           ) {
}
