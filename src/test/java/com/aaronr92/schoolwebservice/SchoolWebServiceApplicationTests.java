package com.aaronr92.schoolwebservice;

import com.aaronr92.schoolwebservice.dto.RoleOperation;
import com.aaronr92.schoolwebservice.entity.Group;
import com.aaronr92.schoolwebservice.repository.GroupRepository;
import com.aaronr92.schoolwebservice.repository.MarkRepository;
import com.aaronr92.schoolwebservice.repository.SubjectRepository;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import com.aaronr92.schoolwebservice.util.Action;
import com.aaronr92.schoolwebservice.util.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
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

import java.lang.reflect.Field;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Autowired
    private GroupRepository groupRepository;

    private final Random random = new Random();

    //reset database
    @Test
    @Order(1)
    void resetDb() {
        userRepository.deleteAll();
        groupRepository.deleteAll();
        markRepository.deleteAll();
        subjectRepository.deleteAll();

        assertThat(userRepository.findAll().size())
                .isEqualTo(0);
        assertThat(groupRepository.findAll().size())
                .isEqualTo(0);
        assertThat(markRepository.findAll().size())
                .isEqualTo(0);
        assertThat(subjectRepository.findAll().size())
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
    void addNewGroupAnonymous() throws Exception{
        mvc.perform(post("/api/group")
                .content(toJson(Group.builder()
                        .groupNumber(0)
                        .groupName("0-teachers")
                        .build())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(4)
    void addNewGroupTeacherPerm() throws Exception{
        mvc.perform(post("/api/group").with(user(email).password(password).roles("TEACHER"))
                .content(toJson(Group.builder()
                        .groupNumber(0)
                        .groupName("0-teachers")
                        .build())))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    void addNewTeacherGroupAdminPerm() throws Exception{
        mvc.perform(post("/api/group").with(user(email).password(password).roles("ADMINISTRATOR"))
                .content(toJson(Group.builder()
                        .groupNumber(0)
                        .groupName("0-teachers")
                        .build())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.group_number", is(0)))
                .andExpect(jsonPath("$.group_name", is("0-teachers")));
    }

    @Test
    @Order(6)
    void addNewStudentGroupAdminPerm() throws Exception{
        mvc.perform(post("/api/group").with(user(email).password(password).roles("ADMINISTRATOR"))
                .content(toJson(Group.builder()
                        .groupNumber(22)
                        .groupName("22-Д9-3ИНС")
                        .build())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.group_number", is(22)))
                .andExpect(jsonPath("$.group_name", is("22-Д9-3ИНС")));
    }

    @Test
    @Order(7)
    void addExistingStudentGroupAdminPerm() throws Exception{
        mvc.perform(post("/api/group").with(user(email).password(password).roles("ADMINISTRATOR"))
                .content(toJson(Group.builder()
                        .groupNumber(22)
                        .groupName("22-Д9-3ИНС")
                        .build())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    void registerNewUser_1() throws Exception {
        mvc.perform(post("/api/user/signup")
                .content(toJson(TestUserDTO.builder()
                            .name("Madeline")
                            .lastname("Sweet")
                            .email("MadelineSw@gmail.com")
                            .group(22)
                            .number_by_order(1)
                            .password("password")
                            .phone("+79181451617")
                            .gender(Gender.FEMALE)
                            .dob("01.02.2006")
                            .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(9)
    void registerNewUser_2() throws Exception {
        mvc.perform(post("/api/user/signup")
                .content(toJson(TestUserDTO.builder()
                            .name("Alec ")
                            .lastname("Huang")
                            .email("alechuang@gmail.com")
                            .group(0)
                            .number_by_order(1)
                            .password("password")
                            .phone("+79181451618")
                            .gender(Gender.MALE)
                            .dob("14.02.2006")
                            .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(10)
    void registerUserWithoutRequestBody() throws Exception {
        mvc.perform(post("/api/user/signup"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    void changeRoleForTeacherWithAuth() throws Exception {
        mvc.perform(put("/api/user/change/role").with(user(email).password(password).roles("ADMINISTRATOR"))
                .content(toJson(new RoleOperation("0_1", "teacher", Action.GRANT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    void changeRoleForTeacherWithoutAuth() throws Exception {
        mvc.perform(put("/api/user/change/role")
                .content(toJson(new RoleOperation("0_1", "teacher", Action.GRANT)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(13)
    void changeRoleForTeacherWithAuthAndExistingRole() throws Exception {
        mvc.perform(put("/api/user/change/role").with(user(email).password(password).roles("ADMINISTRATOR"))
                        .content(toJson(new RoleOperation("0_1", "teacher", Action.GRANT)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(14)
    void changeRoleForTeacherWithAuthAndInvalidRole() throws Exception {
        mvc.perform(put("/api/user/change/role").with(user(email).password(password).roles("ADMINISTRATOR"))
                        .content(toJson(new RoleOperation("0_1", "bot", Action.GRANT)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(15)
    void changeUsernameForTeacherWithStudentAuth() throws Exception {
        mvc.perform(put("/api/user/change/username")
                        .param("old_username", "   0_1")
                        .param("new_username", " alechuang ").with(user(email).password(password).roles("STUDENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(16)
    void changeUsernameForTeacherWithTeacherAuth() throws Exception {
        mvc.perform(put("/api/user/change/username")
                        .param("old_username", "0_1")
                        .param("new_username", "alechuang").with(user(email).password(password).roles("TEACHER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(17)
    void changeUsernameForTeacherWithAdminAuth() throws Exception {
        mvc.perform(put("/api/user/change/username")
                        .param("old_username", "   0_1")
                        .param("new_username", " alechuang ").with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(18)
    void checkTeacherUsername() throws Exception {
        mvc.perform(get("/api/user/find")
                .param("username", "  alechuang ")
                .with(user(email).password(password).roles("STUDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alec")))
                .andExpect(jsonPath("$.lastname", is("Huang")))
                .andExpect(jsonPath("$.email", is("alechuang@gmail.com")));
    }

    @Test
    @Order(19)
    void signupNewStudentToGroup22() throws Exception {
        mvc.perform(post("/api/user/signup")
                        .content(toJson(TestUserDTO.builder()
                                .name("Aiden ")
                                .lastname("Lozano")
                                .email("aidenLoz@gmail.com")
                                .group(22)
                                .number_by_order(2)
                                .password("password")
                                .phone("+79181451619")
                                .gender(Gender.MALE)
                                .dob("08.11.2006")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(20)
    void findGroupWithoutParamsUsingTeacherRole() throws Exception {
        mvc.perform(get("/api/group").with(user(email).password(password).roles("TEACHER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(21)
    void findUnregisteredGroupUsingNumberAndAdminRole() throws Exception {
        mvc.perform(get("/api/group")
                        .param("number", "1")
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(22)
    void findUnregisteredGroupUsingNameAndTeacherRole() throws Exception {
        mvc.perform(get("/api/group")
                        .param("name", "SomeGroupName")
                        .with(user(email).password(password).roles("TEACHER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(23)
    void find22ndGroupUsingNumberAndTeacherRole() throws Exception {
        mvc.perform(get("/api/group")
                        .param("number", "22")
                        .with(user(email).password(password).roles("TEACHER")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(24)
    void find22ndGroupUsingNameAndAdminRole() throws Exception {
        mvc.perform(get("/api/group")
                        .param("name", "22-Д9-3ИНС")
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(25)
    void check22ndGroupsContent() throws Exception {
        mvc.perform(get("/api/group")
                    .param("number", "22")
                    .with(user(email).password(password).roles("TEACHER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].username", is("22_1")))
                .andExpect(jsonPath("$.users[1].username", is("22_2")))
                .andExpect(jsonPath("$.users[0].name", is("Madeline")))
                .andExpect(jsonPath("$.group_number", is(22)));
    }

    @Test
    @Order(26)
    void signupUserWithWrongGroup() throws Exception {
        mvc.perform(post("/api/user/signup")
                        .content(toJson(TestUserDTO.builder()
                                .name("Alena  ")
                                .lastname("Lu")
                                .email("alenakaLulu@gmail.com")
                                .group(23)
                                .number_by_order(1)
                                .password("password")
                                .phone("+79181451620")
                                .gender(Gender.FEMALE)
                                .dob("02.02.2000")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Group does not exist!", result.getResponse().getErrorMessage()));
    }

    @Test
    @Order(27)
    void find22ndGroupWithStudent() throws Exception {
        mvc.perform(get("/api/group")
                    .with(user(email).password(password).roles("STUDENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(28)
    void changeExistingTeacherUsernameWithAdmin() throws Exception {
        mvc.perform(put("/api/user/change/username")
                    .param("old_username", "alechuang")
                    .param("new_username", "alechuang")
                    .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Username [alechuang] is already occupied", result.getResponse().getErrorMessage()));
    }

    /*
    TODO
        Subject tests
     */

    private <T> String toJson(T object) throws IllegalAccessException, JSONException {
        JSONObject json = new JSONObject();
        Class<?> cl = object.getClass();
        for (Field field :
                cl.getDeclaredFields()) {
            field.setAccessible(true);
            JsonProperty fName = field.getAnnotation(JsonProperty.class);
            json.put(fName == null ? field.getName() : fName.value(),
                    field.get(object));
        }
        return json.toString();
    }

}
