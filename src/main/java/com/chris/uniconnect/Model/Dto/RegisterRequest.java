package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Dto.Response.AuthCreateUserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private AuthCreateUserRequest user;

    private PersonDto person;
}
