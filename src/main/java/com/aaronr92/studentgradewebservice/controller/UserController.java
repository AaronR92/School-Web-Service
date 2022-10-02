package com.aaronr92.studentgradewebservice.controller;

import com.aaronr92.studentgradewebservice.dto.PasswordChange;
import com.aaronr92.studentgradewebservice.dto.RoleOperation;
import com.aaronr92.studentgradewebservice.dto.UserDTO;
import com.aaronr92.studentgradewebservice.entity.User;
import com.aaronr92.studentgradewebservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody UserDTO user) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/user/register")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.signup(user));
    }

    @PutMapping("/change/role")
    public ResponseEntity<Void> changeRole(@RequestBody RoleOperation roleOperation) {
        userService.changeRole(roleOperation);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change/username")
    public ResponseEntity<Void> changeUsername(@RequestParam("old_username") String oldUsername,
                                        @RequestParam("new_username") String newUsername) {
        userService.changeUsername(oldUsername, newUsername);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change/password")
    public ResponseEntity<PasswordChange> changePassword(Principal user,
                                                         @RequestBody PasswordChange passwordChange) {
        return ResponseEntity.ok().body(userService.changePassword(user, passwordChange));
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/find")
    public ResponseEntity<User> getUser(@RequestParam String username) {
        return ResponseEntity.ok(((User) userService.loadUserByUsername(username)));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteUser(@RequestParam String username) {
        return new ResponseEntity<>(userService.deleteUser(username), HttpStatus.NO_CONTENT);
    }
}
