package com.chris.uniconnect.Controller;

import com.chris.uniconnect.Model.Entity.UserEntity;
import com.chris.uniconnect.Repository.UserRepository;
import com.chris.uniconnect.Service.Impl.UserDetailsServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class UserConfigController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<UserEntity> getAllUser = userDetailsService.getUsers();
        return new ResponseEntity<>(getAllUser, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user-blocked/{id}")
    public ResponseEntity<?> studentBlocked(@PathVariable Integer id) {
        return new ResponseEntity<>(userDetailsService.userBlocked(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}/{password}")
    public ResponseEntity<?> updatePassword(@PathVariable String username, @PathVariable String password) {
        try {
            return new ResponseEntity<>(userDetailsService.updatePassword(username, password), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
