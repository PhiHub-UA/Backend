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
import deti.tqs.phihub.controllers.patient.AppointmentController;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.UserService;
import deti.tqs.phihub.services.TicketService;
import deti.tqs.phihub.services.ReceptionDeskService;
import deti.tqs.phihub.services.QueueLineService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)

class AppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppointmentService service;
    @MockBean
    private UserService userService;
    @MockBean
    private SpecialityService specialityService;
    @MockBean
    private TicketService ticketService;
    @MockBean
    private ReceptionDeskService receptionDeskService;
    @MockBean
    private QueueLineService queueLineService;
    @MockBean
    private MedicService medicService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private Appointment app0 = new Appointment();
    private User user0 = new User();
    private Medic medic0 = new Medic();

    @BeforeEach
    public void setUp() {
        //  Create a user
        user0.setId(1L);
        user0.setEmail("jose@fino.com");
        user0.setPhone("919828737");

        //  Create a appointment
        app0.setId(1L);
        app0.setSpeciality(specialityService.getSpecialityById(1));
        app0.setPrice(12.3);
        app0.setPatient(user0);
        app0.setMedic(medic0);
        

        when(service.save(Mockito.any())).thenReturn(app0);
        when(service.getAppointmentsByPatient(user0)).thenReturn(List.of(app0));
        when(service.getAppointmentById(app0.getId())).thenReturn(app0);

        when(userService.getUserFromContext()).thenReturn(user0);
        when(medicService.getMedicById(anyLong())).thenReturn(medic0);
        
        when(specialityService.getSpecialityById(1)).thenReturn(Speciality.PULMONOLOGY);
        when(specialityService.getSpecialityById(2)).thenReturn(null);
    }

    @Test
    void whenPostValidAppointment_thenCreateAppointment() throws Exception {
        mvc.perform(
                post("/patient/appointments").contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\": \"1714159929000\"" +
                         ",\"price\":" + app0.getPrice().toString() +
                         ",\"specialityId\": 1" +
                         ",\"medicID\": 1 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price", is(app0.getPrice())));

        verify(service, times(1)).save(Mockito.any());

    }

    @Test
    void givenOneAppointments_thenReturnIt() throws Exception {

        mvc.perform(
                get("/patient/appointments/" + app0.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(app0.getPrice())));

        verify(service, times(1)).getAppointmentById(Mockito.any());
    }

    @Test
    void givenOneAppointments_thenDeleteIt() throws Exception {

        mvc.perform(
                delete("/patient/appointments/" + app0.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteAppointmentById(Mockito.any());
    }

    @Test
    void givenManyAppointments_thenReturnInJsonArray() throws Exception {
        Appointment appointment0 = new Appointment();
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();

        appointment0.setPrice(10.72);
        appointment1.setPrice(16.98);
        appointment2.setPrice(23.53);

        List<Appointment> allAppointments = Arrays.asList(appointment0, appointment1, appointment2);

        when(service.getAppointmentsByPatient(user0)).thenReturn(allAppointments);

        mvc.perform(
                get("/patient/appointments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].price", is(appointment0.getPrice())))
                .andExpect(jsonPath("$[1].price", is(appointment1.getPrice())))
                .andExpect(jsonPath("$[2].price", is(appointment2.getPrice())));

        verify(service, times(1)).getAppointmentsByPatient(user0);
    }

    //  Tests for bad conditions
    @Test
    void givenBadValue_whenAdd_thenReturnError() throws Exception {
        //  Check bad speciality ID
        mvc.perform(
                post("/patient/appointments").contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\": \"1714159929000\"" +
                         ",\"price\":" + app0.getPrice().toString() +
                         ",\"specialityId\": 2}"))
                .andExpect(status().isBadRequest());

        //  Check bad logged in user
        when(userService.getUserFromContext()).thenReturn(null);
        mvc.perform(
                post("/patient/appointments").contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\": \"1714159929000\"" +
                         ",\"price\":" + app0.getPrice().toString() +
                         ",\"specialityId\": 1}"))
                .andExpect(status().isUnauthorized());

        verify(service, times(0)).save(Mockito.any());
    }

    @Test
    void givenBadUser_thenDoNotDeleteAppointment() throws Exception {
        //  Check bad logged in user
        User user1 = new User();
        user1.setId(2L);
        when(userService.getUserFromContext()).thenReturn(user1);

        mvc.perform(
                delete("/patient/appointments/" + app0.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(service, times(0)).deleteAppointmentById(Mockito.any());
    }

    @Test
    void givenBadUser_whenGetByID_thenReturnError() throws Exception {
        //  Check wrong appointment user
        User user1 = new User();
        user1.setId(2L);
        when(userService.getUserFromContext()).thenReturn(user1);

        mvc.perform(
                get("/patient/appointments/" + app0.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}