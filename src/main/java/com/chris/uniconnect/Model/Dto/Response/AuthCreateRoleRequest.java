package com.chris.uniconnect.Model.Dto.Response;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record AuthCreateRoleRequest(
        @Size(max = 1, message = "El usuario no puede tener mas de 1 rol") List<String> roleListName) {
}
