package deti.tqs.phihub.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.phihub.configs.SecurityFilter;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.controllers.patient.UserController;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.StaffService;
import deti.tqs.phihub.services.UserService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @MockBean
    private MedicService medicService;
    @MockBean
    private StaffService staffService;

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
    public void setUp() {
        //  Create a user
        user0.setId(1L);
        user0.setUsername("josefino");
        user0.setEmail("jose@fino.com");
        user0.setPhone("919828737");
        
        when(service.getUserFromContext()).thenReturn(user0);
        when(service.seeIfLoggedIn()).thenReturn(true);
        when(service.getUserById(Mockito.any())).thenReturn(user0);
    }

    @Test
    void givenOneUsers_thenReturnIt() throws Exception {

        mvc.perform(
                get("/patient/users/" + user0.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone", is(user0.getPhone())));

        verify(service, times(1)).getUserById(Mockito.any());
    }

    @Test
    void givenOneUsersLoggedIn_thenReturnIt() throws Exception {

        mvc.perform(
                get("/patient/users/me").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone", is(user0.getPhone())));

        verify(service, times(1)).getUserFromContext();
    }

    //  Tests for bad conditions
    @Test
    void givenBadUserID_whenGet_thenReturnError() throws Exception {
        //  Check user permissions
        when(service.getUserById(2L)).thenReturn(null);
        
        mvc.perform(
            get("/patient/users/2").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenUserNotLoggedIn_whenGet_thenReturnError() throws Exception {
        //  Check user bad logged in
        when(service.getUserFromContext()).thenReturn(null);
        
        mvc.perform(
            get("/patient/users/me").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void givenBadUserPermissions_whenGet_thenReturnError() throws Exception {
        //  Check user permissions
        User user1 = new User();
        user1.setUsername("badActor");
        when(service.getUserFromContext()).thenReturn(user1);
        
        mvc.perform(
            get("/patient/users/" + user0.getId().toString()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
}