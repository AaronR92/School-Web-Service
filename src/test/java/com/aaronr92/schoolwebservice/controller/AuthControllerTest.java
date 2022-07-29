package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.service.UserService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockBean
    public UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @Rollback(false)
    @Order(1)
    void registerFirstUser() throws Exception {
        mvc
                .perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\": \"Anton\"," +
                        "\"lastname\": \"Ruzanov\"," +
                        "\"email\": \"ruzanov.@gmail.com\"," +
                        "\"phone\": \"+79181451681\"," +
                        "\"dob\": \"15.09.2000\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    @Rollback(false)
    @Order(2)
    void registerFirstUserTwice() throws Exception {
        mvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\": \"Anton\"," +
                                "\"lastname\": \"Ruzanov\"," +
                                "\"email\": \"ruzanov@gmail.com\"," +
                                "\"phone\": \"+79181451681\"," +
                                "\"dob\": \"15.09.2000\"" +
                                "}"))
                .andExpect(status().isBadRequest());
    }
}
