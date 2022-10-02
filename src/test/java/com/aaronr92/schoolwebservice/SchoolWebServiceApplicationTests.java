package com.aaronr92.schoolwebservice;

import com.aaronr92.schoolwebservice.dto.MarkDTO;
import com.aaronr92.schoolwebservice.dto.PasswordChange;
import com.aaronr92.schoolwebservice.dto.RoleOperation;
import com.aaronr92.schoolwebservice.dto.SubjectDTO;
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

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;

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

    @Autowired
    DataSource dataSource;

    @Test
    @Order(1)
    void cleanDB() throws SQLException {
        markRepository.deleteAll();
        subjectRepository.deleteAll();
        userRepository.deleteAll();
        groupRepository.deleteAll();

        dataSource.getConnection().prepareStatement("UPDATE group_seq SET next_val=1;").execute();
        dataSource.getConnection().prepareStatement("UPDATE mark_seq SET next_val=1;").execute();
        dataSource.getConnection().prepareStatement("UPDATE subject_seq SET next_val=1;").execute();
        dataSource.getConnection().prepareStatement("UPDATE user_seq SET next_val=1;").execute();

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
    void registerNewUser_3() throws Exception {
        mvc.perform(post("/api/user/signup")
                        .content(toJson(TestUserDTO.builder()
                                .name("Aiden ")
                                .lastname("Lozano")
                                .email("aidenLoz@gmail.com")
                                .group(22)
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

    @Test
    @Order(29)
    void addNewNotValidSubject() throws Exception {
        mvc.perform(post("/api/subject")
                    .content(toJson(SubjectDTO.builder()
                            .subject_name("Math")
                            .build()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(30)
    void addNewSubjectWithTeacher() throws Exception {
        mvc.perform(post("/api/subject")
                    .content(toJson(SubjectDTO.builder()
                            .subject_name("Math")
                            .teacher_username("alechuang")
                            .build()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user(email).password(password).roles("TEACHER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(31)
    void addNewSubjectWithAdmin() throws Exception {
        mvc.perform(post("/api/subject")
                    .content(toJson(SubjectDTO.builder()
                            .subject_name("Math")
                            .teacher_username("alechuang")
                            .build()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(32)
    void addNewSubjectWrongTeacherUsernameWithAdmin() throws Exception {
        mvc.perform(post("/api/subject")
                        .content(toJson(SubjectDTO.builder()
                                .subject_name("English")
                                .teacher_username("america")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(result -> assertEquals("This teacher is not registered!", result.getResponse().getErrorMessage()));
    }

    @Test
    @Order(33)
    void addNewSubjectStudentUsernameWithAdmin() throws Exception {
        mvc.perform(post("/api/subject")
                        .content(toJson(SubjectDTO.builder()
                                .subject_name("English")
                                .teacher_username("22_2")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Only teachers can lead subjects!", result.getResponse().getErrorMessage()));
    }

    @Test
    @Order(34)
    void registerNewUser_4() throws Exception {
        mvc.perform(post("/api/user/signup")
                        .content(toJson(TestUserDTO.builder()
                                .name("Filip")
                                .lastname("York")
                                .email("fillipYo@gmail.com")
                                .group(22)
                                .password("password")
                                .phone("+79181451620")
                                .gender(Gender.MALE)
                                .dob("27.07.2006")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(35)
    void registerNewUser_5() throws Exception {
        mvc.perform(post("/api/user/signup")
                        .content(toJson(TestUserDTO.builder()
                                .name("Alice ")
                                .lastname("Flores")
                                .email("aliceFlor@gmail.com")
                                .group(0)
                                .password("password")
                                .phone("+79181451621")
                                .gender(Gender.FEMALE)
                                .dob("14.06.1990")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(36)
    void changeUsernameToUser5() throws Exception {
        mvc.perform(put("/api/user/change/username")
                        .param("old_username", "0_2")
                        .param("new_username", "aliceflores").with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(37)
    void changeRoleToUser5() throws Exception {
        mvc.perform(put("/api/user/change/role").with(user(email).password(password).roles("ADMINISTRATOR"))
                        .content(toJson(new RoleOperation("aliceflores", "tEaCHeR", Action.GRANT)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(38)
    void addNewEnglishSubject() throws Exception {
        mvc.perform(post("/api/subject")
                        .content(toJson(SubjectDTO.builder()
                                .subject_name("English")
                                .teacher_username("aliceflores")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(39)
    void addNewBiologySubjectWithoutTeacher() throws Exception {
        mvc.perform(post("/api/subject")
                        .content(toJson(SubjectDTO.builder()
                                .subject_name("Biology")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(40)
    void registerNewUser_6() throws Exception {
        mvc.perform(post("/api/user/signup")
                        .content(toJson(TestUserDTO.builder()
                                .name("Penelope  ")
                                .lastname("Lyon")
                                .email("penelopeLyon@gmail.com")
                                .group(0)
                                .password("password")
                                .phone("+79181451622")
                                .gender(Gender.FEMALE)
                                .dob("21.10.1984")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(41)
    void changeUsernameToUser6() throws Exception {
        mvc.perform(put("/api/user/change/username")
                        .param("old_username", "0_3")
                        .param("new_username", "penelopeLyon").with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(42)
    void changeRoleToUser6() throws Exception {
        mvc.perform(put("/api/user/change/role").with(user(email).password(password).roles("ADMINISTRATOR"))
                        .content(toJson(new RoleOperation("penelopeLyon", "tEaCHeR", Action.GRANT)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(43)
    void addNewBiologySubject() throws Exception {
        mvc.perform(post("/api/subject")
                        .content(toJson(SubjectDTO.builder()
                                .subject_name("Biology")
                                .teacher_username("penelopelyon")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(44)
    void addTeacher6ToBiology() throws Exception {
        mvc.perform(put("/api/subject/3")
                        .param("operation", "ADD")
                        .param("teacher", "penelopelyon")
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Teacher is already teaching this subject!",
                        result.getResponse().getErrorMessage()));
    }

    @Test
    @Order(45)
    void removeTeacher6FromBiology() throws Exception {
        mvc.perform(put("/api/subject/3")
                        .param("operation", "REMOVE")
                        .param("teacher", "penelopelyon")
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("The Teacher has been removed",
                        result.getResponse().getErrorMessage()));
    }

    @Test
    @Order(46)
    void addTeacher6ToEnglish() throws Exception {
        mvc.perform(put("/api/subject/2")
                        .param("operation", "ADD")
                        .param("teacher", "penelopelyon")
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(47)
    void changePassword() throws Exception {
        mvc.perform(put("/api/user/change/password")
                    .content(toJson(PasswordChange.builder()
                            .currentPassword("password")
                            .newPassword("p4ssword")
                            .build()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("alechuang").password("password").roles("TEACHER")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(48)
    void changePasswordBack() throws Exception {
        mvc.perform(put("/api/user/change/password")
                    .content(toJson(PasswordChange.builder()
                            .currentPassword("p4ssword")
                            .newPassword("password")
                            .build()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("alechuang").password("p4ssword").roles("TEACHER")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(49)
    void postMarkWithAdmin() throws Exception {
        mvc.perform(post("/api/student/mark")
                        .content(toJson(MarkDTO.builder()
                                .subjectName("Math")
                                .student("22_2")
                                .mark(5)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("alechuang").password("password").roles("ADMINISTRATOR")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(50)
    void postMarkWithTeacher() throws Exception {
        mvc.perform(post("/api/student/mark")
                    .content(toJson(MarkDTO.builder()
                            .subjectName("Math")
                            .student("22_2")
                            .mark(5)
                            .build()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(user("alechuang").password("password").roles("TEACHER")))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("{\"status\":\"success\"}", result.getResponse().getContentAsString()));
    }

    @Test
    @Order(51)
    void findUser22_2() throws Exception {
        mvc.perform(get("/api/user/find")
                        .param("username", "22_2")
                        .with(user(email).password(password).roles("ADMINISTRATOR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("aidenloz@gmail.com")))
                .andExpect(jsonPath("$.roles[0]", is("ROLE_STUDENT")));
    }

    @Test
    @Order(52)
    void deleteMarkWithTeacher() throws Exception {
        mvc.perform(delete("/api/student/mark")
                        .content(toJson(MarkDTO.builder()
                                .subjectName("Math")
                                .student("22_2")
                                .mark(5)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("alechuang").password("password").roles("TEACHER")))
                .andExpect(status().isNoContent());
    }

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
