package deti.tqs.phihub.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.phihub.configs.SecurityFilter;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.controllers.SpecialityController;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

@WebMvcTest(SpecialityController.class)
@AutoConfigureMockMvc(addFilters = false)
class SpecialityControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SpecialityService service;

    @MockBean
    private UserService userService;
    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    @BeforeEach
    public void setUp() throws Exception {
        when(service.getSpecialities()).thenReturn(List.of(Speciality.PULMONOLOGY.toString(), Speciality.PEDIATRICS.toString()));
    }

    @Test
    void givenSpecialities_thenReturnThem() throws Exception {

        mvc.perform(
                get("/speciality").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).getSpecialities();
    }
}