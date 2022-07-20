package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.dto.PasswordChange;
import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    ResponseEntity<User> register(@Valid @RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/auth/signup")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.registerNewUser(user));
    }

    //TODO
    // change password method

    @PutMapping("/change/password")
    ResponseEntity<PasswordChange> changePassword() {
        return null;
    }
}
