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
import deti.tqs.phihub.controllers.patient.TicketController;
import deti.tqs.phihub.dtos.TicketReturnSchema;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.models.WaitingRoom;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.TicketService;
import deti.tqs.phihub.services.UserService;
import deti.tqs.phihub.services.WaitingRoomService;
import deti.tqs.phihub.services.QueueLineService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TicketService service;

    @MockBean
    private UserService userService;
    @MockBean
    private QueueLineService queueLineService;
    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private WaitingRoomService waitingRoomService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private User user0 = new User();
    private Appointment app0 = new Appointment();
    private QueueLine queue0 = new QueueLine();
    private WaitingRoom waitRoom0 = new WaitingRoom();

    @BeforeEach
    public void setUp() {
        //  Create a user
        user0.setId(1L);
        user0.setUsername("josefino");
        user0.setEmail("jose@fino.com");
        user0.setPhone("919828737");

        app0.setId(1L);

        TicketReturnSchema tick0Schema = new TicketReturnSchema(1L,1L,"A",1,1L);
        
        when(userService.getUserFromContext()).thenReturn(user0);
        when(appointmentService.getAppointmentById(Mockito.any())).thenReturn(app0);
        when(queueLineService.getQueueLineById(Mockito.any())).thenReturn(queue0);
        when(waitingRoomService.getWaitingRoomById(Mockito.any())).thenReturn(waitRoom0);
        when(service.createTicket(Mockito.any(), Mockito.any())).thenReturn(tick0Schema);

    }

    @Test
    void givenOneTickets_thenAddIt() throws Exception {
        mvc.perform(
                post("/patient/tickets").contentType(MediaType.APPLICATION_JSON)
                .content("{\"priority\": false" +
                         ",\"appointmentId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueLineLetter", is("A")));

        verify(service, times(1)).createTicket(Mockito.any(), Mockito.any());
    }

    @Test
    void givenBadAppointment_thenReturnError() throws Exception {
        when(appointmentService.getAppointmentById(Mockito.any())).thenReturn(null);

        mvc.perform(
                post("/patient/tickets").contentType(MediaType.APPLICATION_JSON)
                .content("{\"priority\": false" +
                         ",\"appointmentId\": 1}"))
                .andExpect(status().isNotFound());

        verify(service, times(0)).createTicket(Mockito.any(), Mockito.any());
    }

    @Test
    void givenBadTicket_thenReturnError() throws Exception {
        when(service.createTicket(Mockito.any(), Mockito.any())).thenReturn(null);

        mvc.perform(
                post("/patient/tickets").contentType(MediaType.APPLICATION_JSON)
                .content("{\"priority\": false" +
                         ",\"appointmentId\": 1}"))
                .andExpect(status().isBadRequest());
    }
}