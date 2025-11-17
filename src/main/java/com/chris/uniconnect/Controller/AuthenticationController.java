package com.chris.uniconnect.Controller;


import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.RegisterRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthCreateUserRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthLoginRequest;
import com.chris.uniconnect.Model.Dto.Response.AuthResponse;
import com.chris.uniconnect.Service.Impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        AuthCreateUserRequest authCreateUserRequest = registerRequest.getUser();
        PersonDto person = registerRequest.getPerson();
        return new ResponseEntity<>(this.userDetailsService.createUser(authCreateUserRequest , person), HttpStatus.CREATED);
    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {

        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }


}
