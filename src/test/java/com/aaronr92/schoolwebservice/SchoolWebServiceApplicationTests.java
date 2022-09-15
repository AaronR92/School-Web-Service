package com.aaronr92.schoolwebservice;

import com.aaronr92.schoolwebservice.dto.RoleOperation;
import com.aaronr92.schoolwebservice.dto.UserDTO;
import com.aaronr92.schoolwebservice.repository.MarkRepository;
import com.aaronr92.schoolwebservice.repository.SubjectRepository;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import com.aaronr92.schoolwebservice.util.Action;
import com.aaronr92.schoolwebservice.util.Gender;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        mvc.perform(get("/api/user/find/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    void registerNewUser_1() throws Exception {
        mvc.perform(post("/api/user/signup")
                .content(userToJson(UserDTO.builder()
                            .name("Madeline")
                            .lastname("Sweet")
                            .email("MadelineSw@gmail.com")
                            .group(22)
                            .numberByOrder(1)
                            .password("password")
                            .phone("+79181451617")
                            .gender(Gender.FEMALE)
                            .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    void registerNewUser_2() throws Exception {
        mvc.perform(post("/api/user/signup")
                .content(userToJson(UserDTO.builder()
                            .name("Alec ")
                            .lastname("Huang")
                            .email("alechuang@gmail.com")
                            .group(0)
                            .numberByOrder(1)
                            .password("password")
                            .phone("+79181451618")
                            .gender(Gender.MALE)
                            .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(4)
    void registerUserWithoutRequestBody() throws Exception {
        mvc.perform(post("/api/user/signup"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void changeRoleForTeacherWithAuth() throws Exception {
        mvc.perform(put("/api/user/change/role").with(user(email).password(password).roles("ADMINISTRATOR"))
                .content(roleOperationToJson(new RoleOperation("0_1", "teacher", Action.GRANT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void changeRoleForTeacherWithoutAuth() throws Exception {
        mvc.perform(put("/api/user/change/role")
                .content(roleOperationToJson(new RoleOperation("0_1", "teacher", Action.GRANT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(7)
    void changeRoleForTeacherWithAuthAndExistingRole() throws Exception {
        mvc.perform(put("/api/user/change/role").with(user(email).password(password).roles("ADMINISTRATOR"))
                        .content(roleOperationToJson(new RoleOperation("0_1", "teacher", Action.GRANT)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    void changeRoleForTeacherWithAuthAndInvalidRole() throws Exception {
        mvc.perform(put("/api/user/change/role").with(user(email).password(password).roles("ADMINISTRATOR"))
                        .content(roleOperationToJson(new RoleOperation("0_1", "bot", Action.GRANT)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(9)
    void changeUsernameForTeacherWithStudentAuth() throws Exception {
        mvc.perform(put("/api/user/change/username")
                        .param("old_username", "   0_1")
                        .param("new_username", " alechuang ").with(user(email).password(password).roles("STUDENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
    void changeUsernameForTeacherWithTeacherAuth() throws Exception {
        mvc.perform(put("/api/user/change/username")
                        .param("old_username", "0_1")
                        .param("new_username", "alechuang").with(user(email).password(password).roles("TEACHER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(11)
    void changeUsernameForTeacherWithAdminAuth() throws Exception {
        mvc.perform(put("/api/user/change/username")
                        .param("old_username", "   0_1")
                        .param("new_username", " alechuang ").with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    void checkTeacherUsername() throws Exception {
        mvc.perform(get("/api/user/find")
                .param("username", "  alechuang ")
                .with(user(email).password(password).roles("STUDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alec")))
                .andExpect(jsonPath("$.lastname", is("Huang")))
                .andExpect(jsonPath("$.email", is("alechuang@gmail.com")));
    }

    private String userToJson(UserDTO user) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", user.getName());
        json.put("lastname", user.getLastname());
        json.put("email", user.getEmail());
        json.put("group", user.getGroup());
        json.put("number_by_order", user.getNumberByOrder());
        json.put("password", user.getPassword());
        json.put("phone", user.getPhone());
        json.put("dob", String.format("%d.0%d.%d", random.nextInt(18) + 10, random.nextInt(9) + 1, random.nextInt(8) + 2006));
        json.put("gender", user.getGender());
        return json.toString();
    }

    private String roleOperationToJson(RoleOperation roleOperation) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("username", roleOperation.getUsername());
        json.put("role", roleOperation.getRole());
        json.put("action", roleOperation.getAction());
        return json.toString();
    }

}
