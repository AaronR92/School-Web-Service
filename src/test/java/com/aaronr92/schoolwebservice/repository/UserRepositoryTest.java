package com.aaronr92.schoolwebservice.repository;

import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.util.Gender;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(value = false)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void ifUserExistsByEmail() {
        // given
        String email = "tylerlacey@gmail.com";
        String phone = "89181231234";
        User student = User.builder()
                .name("Tyler")
                .lastname("Lacey")
                .email(email)
                .phone(phone)
                .dateOfBirth(LocalDate.of(2000, 2, 12))
                .gender(Gender.MALE.name())
                .build();
        userRepository.save(student);
        System.out.println("Saved");

        // when
        boolean exp = userRepository.existsUserByEmailIgnoreCaseAndPhone(email.toUpperCase(), phone);

        // then
        assertTrue(exp);
    }

    @Test
    @Order(2)
    void ifUserDoesNotExistsByEmail() {
        // given
        String email = "Bobby@gmail.com";
        String phone = "89181231231";

        // when
        boolean exp = userRepository.existsUserByEmailIgnoreCaseAndPhone(email, phone);

        // then
        assertFalse(exp);
    }

    @Test
    @Order(3)
    void ifUserDoesExistsByEmailIgnoreCase() {
        // given
        String email = "tylerlacey@gmail.com";
        String phone = "8918123124";

        // when
        boolean exp = userRepository.existsUserByEmailIgnoreCaseAndPhone(email, phone);

        // then
        assertTrue(exp);
    }
}