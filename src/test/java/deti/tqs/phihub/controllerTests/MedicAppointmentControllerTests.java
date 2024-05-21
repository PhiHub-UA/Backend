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
import deti.tqs.phihub.controllers.medic.MedicAppointmentController;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.UserService;
import deti.tqs.phihub.services.MedicService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(MedicAppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class MedicAppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MedicService service;

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

    private Medic medic0 = new Medic();
    private User user0 = new User();
    private Appointment app0 = new Appointment();

    @BeforeEach
    public void setUp() {
        //  Create a user
        user0.setId(1L);
        user0.setUsername("josefino");
        user0.setEmail("jose@fino.com");
        user0.setPhone("919828737");
        
        //  Create a medic
        medic0.setId(1L);
        medic0.setName("josefino");
        medic0.setUsername("jose_fino");
        medic0.setSpecialities(List.of(Speciality.CARDIOLOGY, Speciality.HEMATOLOGY));
        
        //  Create a appointment
        app0.setId(1L);
        app0.setMedic(medic0);
        app0.setPatient(user0);
        app0.setNotes("Standard appointment");

        when(service.getMedicFromContext()).thenReturn(medic0);
        when(appointmentService.save(Mockito.any())).thenReturn(app0);
        when(appointmentService.getAppointmentById(Mockito.any())).thenReturn(app0);
    }

    @Test
    void givenPostValidNote_thenReturnIt() throws Exception {
        mvc.perform(
                post("/medic/appointments/1/notes").contentType(MediaType.APPLICATION_JSON)                
                .content("{\"notes\": \"The patient is all good\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes", is("The patient is all good")));

        verify(appointmentService, times(1)).getAppointmentById(Mockito.any());
    }

    @Test
    void givenGetValidNote_thenReturnIt() throws Exception {

        mvc.perform(
                post("/medic/appointments/1/notes").contentType(MediaType.APPLICATION_JSON)                
                .content("{\"notes\": \"The patient is all good\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes", is("The patient is all good")));

        mvc.perform(
                get("/medic/appointments/1/notes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes", is("The patient is all good")));

        verify(appointmentService, times(2)).getAppointmentById(Mockito.any());
    }

    @Test
    void givenGivvenBadLogin_thenReturnErrors() throws Exception {

        when(service.getMedicFromContext()).thenReturn(null);

        mvc.perform(
                post("/medic/appointments/1/notes").contentType(MediaType.APPLICATION_JSON)                
                .content("{\"notes\": \"The patient is all good\"}"))
                .andExpect(status().isUnauthorized());

        mvc.perform(
                get("/medic/appointments/1/notes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(appointmentService, times(0)).getAppointmentById(Mockito.any());
    }

    @Test
    void givenGivvenBadAppointmentID_thenReturnErrors() throws Exception {

        when(appointmentService.getAppointmentById(Mockito.any())).thenReturn(null);

        mvc.perform(
                post("/medic/appointments/1/notes").contentType(MediaType.APPLICATION_JSON)                
                .content("{\"notes\": \"The patient is all good\"}"))
                .andExpect(status().isNotFound());

        mvc.perform(
                get("/medic/appointments/1/notes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(2)).getAppointmentById(Mockito.any());
    }
}