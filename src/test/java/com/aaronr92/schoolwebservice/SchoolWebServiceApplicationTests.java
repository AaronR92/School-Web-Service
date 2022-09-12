package com.aaronr92.schoolwebservice;

import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.repository.MarkRepository;
import com.aaronr92.schoolwebservice.repository.SubjectRepository;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import com.aaronr92.schoolwebservice.util.Gender;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
        MySQLJpaConfig.class, SchoolWebServiceApplication.class
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SchoolWebServiceApplicationTests {

    private final String email = "conor@gmail.com";
    private final String password = "password";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private Random random = new Random();

    //reset database
    @Test
    @Order(1)
    void resetDb() {
        userRepository.deleteAll();
        assertThat(userRepository.findAll().size())
                .isEqualTo(0);
    }

    @Test
    @Order(2)
    void unauthorizedGetRequest() throws Exception {
        mvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    void registerNewUser() throws Exception {
        mvc.perform(post("/api/user/add").with(user(email).password(password).roles("ADMINISTRATOR"))
                .content(userToJson(User.builder()
                            .name("Madeline")
                            .lastname("Sweet")
                            .email("MadelineSw@gmail.com")
                            .username("MadelineSw")
                            .password("password")
                            .phone("+79181451617")
                            .gender(Gender.FEMALE)
                            .isUsernameChanged(false)
                            .isNonLocked(true)
                            .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private String userToJson(User user) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", user.getId());
        json.put("name", user.getName());
        json.put("lastname", user.getLastname());
        json.put("email", user.getEmail());
        json.put("username", user.getUsername());
        json.put("password", user.getPassword());
        json.put("phone", user.getPhone());
        json.put("dob", String.format("%d.0%d.%d", random.nextInt(18) + 10, random.nextInt(9) + 1, random.nextInt(8) + 2006));
        json.put("gender", user.getGender());
        return json.toString();
    }

}
