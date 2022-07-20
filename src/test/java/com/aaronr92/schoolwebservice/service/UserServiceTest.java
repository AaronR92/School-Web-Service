package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import com.aaronr92.schoolwebservice.util.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    @Test
    void canRegisterNewUser() {
        // given
        User student = User.builder()
                .name("Tyler")
                .lastname("Lacey")
                .email("tylerlacey@gmail.com")
                .phone("89181231234")
                .dateOfBirth(LocalDate.of(2000, 2, 12))
                .gender(Gender.MALE.name())
                .build();

        // when
        underTest.registerNewUser(student);

        // then

    }

    @Test
    void deleteUser() {
    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();

        //then
        verify(userRepository).findAll();
    }
}