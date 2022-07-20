package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    /*@GetMapping()
    ResponseEntity<User> getUser(@RequestParam String username) {
        return ResponseEntity.ok().body((User) userService.loadUserByUsername(username));
    }*/
}
