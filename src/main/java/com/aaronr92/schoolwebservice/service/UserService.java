package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.dto.PasswordChange;
import com.aaronr92.schoolwebservice.dto.RoleOperation;
import com.aaronr92.schoolwebservice.dto.UserDTO;
import com.aaronr92.schoolwebservice.entity.Group;
import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.repository.GroupRepository;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import com.aaronr92.schoolwebservice.util.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username.trim());
        if (user.isPresent()){
            log.info("User found in the database");
            return user.get();
        } else {
            log.error("User not found in the database");
            throw new UsernameNotFoundException(String.format("User [%s] not found", username));
        }
    }

    public User signup(UserDTO userDTO) {
        checkValidPassword(userDTO.getPassword());

        Group group = groupRepository.findGroupByGroupNumber(userDTO.getGroup());

        if (group == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group does not exist!");

        String username = String.format("%d_%d", userDTO.getGroup(), group.getUsers().size() + 1);

        if (userRepository.existsUserByEmailIgnoreCase(userDTO.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User with email [%s] already exists!", userDTO.getEmail()));
        if (userRepository.existsUserByUsername(username))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User [%s] already exists!", username));
        if (userRepository.existsUserByPhone(userDTO.getPhone()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User with phone number [%s] already exists!", userDTO.getPhone()));


        User user = User.builder()
                .name(userDTO.getName().trim())
                .lastname(userDTO.getLastname().trim())
                .email(userDTO.getEmail().toLowerCase())
                .username(username)
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .phone(userDTO.getPhone())
                .dateOfBirth(userDTO.getDateOfBirth())
                .gender(userDTO.getGender())
                .group(groupRepository.findGroupByGroupNumber(userDTO.getGroup()))
                .isNonLocked(true)
                .build();

        if (userRepository.count() < 1) {
            user.grantAuthority(Role.ROLE_ADMINISTRATOR);
        } else {
            user.grantAuthority(Role.ROLE_STUDENT);
        }


        log.info("Saving new user {} with username {} to the database",
                user.getName(),
                user.getUsername());

        return userRepository.save(user);
    }

    public Map<String, String> deleteUser(String username) {
        if (!userRepository.existsUserByUsername(username))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!");

        User user = userRepository.findUserByUsername(username).get();

        log.info("Deleting user with name {} and username {}",
                user.getName(),
                user.getUsername());

        userRepository.delete(user);

        return Map.of("status", "User was successfully deleted!");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void changeRole(RoleOperation roleOperation) {
        Optional<User> userOptional = userRepository.findUserByUsername(roleOperation.getUsername());
        Role role = checkRole(roleOperation.getRole());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            switch (roleOperation.getAction()) {
                case GRANT:
                    if (user.getRoles().contains(role)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "The user already has this role!");
                    }
                    if (user.getRoles().contains(Role.ROLE_STUDENT) && role.equals(Role.ROLE_TEACHER)) {
                        user.grantAuthority(Role.ROLE_TEACHER);
                        user.removeAuthority(Role.ROLE_STUDENT);
                    }
                    user.grantAuthority(role);
                    break;

                case REMOVE:
                    if (!user.getRoles().contains(role))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "The user does not have this role!");
                    else
                        user.removeAuthority(role);
                    break;
            }
            userRepository.save(user);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User does not exist!");
    }

    public void changeUsername(String oldUsername, String newUsername) {
        newUsername = newUsername.trim();
        Optional<User> userOptional = userRepository.findUserByUsername(oldUsername.trim());
        if (userOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!");
        if (userRepository.existsUserByUsername(newUsername))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Username [%s] is already occupied", newUsername));
        User user = userOptional.get();
        user.setUsername(newUsername);
        userRepository.save(user);
    }

    public PasswordChange changePassword(Principal principal, PasswordChange passwordChange) {
        User user = userRepository.findUserByUsername(principal.getName()).get();
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

    private void checkValidPassword(String password) {
        if (password.length() < 8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The password length must be at least 8 chars!");
        if (password.length() > 16)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The password length must be shorter than 16 chars!");
    }

    private Role checkRole(String role) {
        Role r = findRole(role);
        if (r == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Role not found!");
        return r;
    }

    private Role findRole(String role) {
        if (role == null)
            return null;

        for (Role r : Role.values()) {
            if (String.format("ROLE_%s", role.toUpperCase()).equals(r.name()))
                return r;
        }

        return null;
    }
}
