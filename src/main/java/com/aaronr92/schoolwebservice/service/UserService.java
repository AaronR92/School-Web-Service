package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.dto.PasswordChange;
import com.aaronr92.schoolwebservice.dto.RoleChanged;
import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import com.aaronr92.schoolwebservice.util.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()){
            log.info("User found in the database");
            return user.get();
        } else {
            log.error("User not found in the database");
            throw new UsernameNotFoundException(String.format("User [%s] not found", username));
        }
    }

    public User registerNewUser(User user) {
        if (userRepository.existsUserByEmailIgnoreCase(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User with email [%s] already exists", user.getEmail()));
        if (userRepository.existsUserByUsername(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Username [%s] already taken!", user.getUsername()));
        if (userRepository.existsUserByPhone(user.getPhone()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User with phone number [%s] already exists", user.getPhone()));

        checkValidPassword(user.getPassword());

        if (userRepository.count() == 0)
            user.grantAuthority(Role.ROLE_ADMINISTRATOR);
        else
            user.grantAuthority(Role.ROLE_STUDENT);

        user.setName(user.getName().trim());
        user.setLastname(user.getLastname().trim());
        user.setUsername(user.getUsername().trim());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNonLocked(true);

        log.info("Saving new user {} with username {} to the database",
                user.getName(),
                user.getUsername());

        return userRepository.save(user);
    }

    public PasswordChange changePassword(User user, PasswordChange passwordChange) {
        if (!passwordEncoder.matches(passwordChange.getCurrentPassword(), user.getPassword())) {
            log.error("Current passwords does not match for user {}", user.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Current password does not match yours!");
        }
        checkValidPassword(passwordChange.getNewPassword());
        if (passwordEncoder.matches(passwordChange.getNewPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You already have this password!");
        }

        String password = passwordEncoder.encode(passwordChange.getNewPassword());
        user.setPassword(password);
        userRepository.save(user);
        passwordChange.setStatus("Password has changed successfully!");
        return passwordChange;
    }

    public Map<String, String> deleteUser(String username) {
        if (!userRepository.existsUserByUsername(username))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");

        User user = userRepository.findUserByUsername(username).get();

        log.info("Deleting user with name {} and username {}",
                user.getName(),
                user.getUsername());

        userRepository.delete(user);

        return Map.of("status", "User was successfully deleted!");
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public RoleChanged changeRole(RoleChanged roleOperation) {
        Optional<User> user = userRepository.findUserByUsername(roleOperation.getUsername());
        Role role = checkRole(roleOperation.getRole());

        if (user.isPresent()) {
            switch (roleOperation.getAction()) {
                case GRANT:
                    if (user.get().getRoles().contains(role))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "The user already has this role!");
                    else
                        user.get().grantAuthority(role);
                    break;

                case REMOVE:
                    if (!user.get().getRoles().contains(role))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "The user does not have this role!");
                    else
                        user.get().removeAuthority(role);
                    break;
            }
            userRepository.save(user.get());
            return roleOperation;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "User does not exist!");
    }

    private void checkValidPassword(String password) {
        if (password.length() < 8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The password length must be at least 8 chars!");
        if (password.length() > 16)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The password length must be shorter than 16 chars!");
    }

    public Role checkRole(String role) {
        for (Role r : Role.values()) {
            if (String.format("ROLE_%s", role).equals(r.name()))
                return r;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Role not found!");
    }
}
