package deti.tqs.phihub.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.phihub.configs.SecurityFilter;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.controllers.AppointmentController;
import deti.tqs.phihub.controllers.AuthController;
import deti.tqs.phihub.controllers.UserController;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.UserService;
import lombok.var;
import deti.tqs.phihub.services.AuthService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthService service;

    @MockBean
    private AuthenticationManager authManager;
    @MockBean
    private UserService userService;
    @MockBean
    private SpecialityService specialityService;
    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private User user0 = new User();

    @BeforeEach
    public void setUp() throws Exception {
        //  Create a user
        user0.setId(1L);
        user0.setName("Josefino");
        user0.setUsername("josefino");
        user0.setEmail("jose@fino.com");
        user0.setPhone("919828737");
        user0.setRole("admin");
        user0.setEmail("jose@gino.com");
        user0.setAge(27);
        user0.setPassword("strongPassword");
        
        when(service.registerUser(Mockito.any())).thenReturn(user0);
        when(tokenProvider.generateAccessToken(Mockito.any())).thenReturn("tokennicezao");
    }

    @Test
    void whenRegisterValidUser_thenCreateUser() throws Exception {
        mvc.perform(
                post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + user0.getName() + "\"," +
                         "\"phone\":\"" + user0.getPhone() + "\"," +
                         "\"email\":\"" + user0.getEmail() + "\"," +
                         "\"age\":\"" + user0.getAge() + "\"," +
                         "\"username\":\"" + user0.getUsername() + "\"," +
                         "\"password\":\"" + user0.getPassword() + "\"," +
                         "\"role\":\"" + user0.getRole() + "\"}"))
                .andExpect(status().isCreated());
    }
    
    /* @Test
    void wheLoginValidUser_thenCreateToken() throws Exception {
        when(authManager.authenticate(Mockito.any())).thenReturn((Authentication)new User());
        mvc.perform(
                post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + user0.getUsername() + "\"" +
                         ",\"password\":\"" + user0.getPassword() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", isA(Map.class)));
    } */
}