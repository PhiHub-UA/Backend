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
import deti.tqs.phihub.controllers.staff.QueueLineController;
import deti.tqs.phihub.dtos.MedicSchema;
import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.ReceptionDesk;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.models.StaffPermissions;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.services.QueueLineService;
import deti.tqs.phihub.services.ReceptionDeskService;
import deti.tqs.phihub.services.StaffService;
import deti.tqs.phihub.services.TicketService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(QueueLineController.class)
@AutoConfigureMockMvc(addFilters = false)
class QueueLineControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QueueLineService service;

    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private QueueLine queue0 = new QueueLine();
    private QueueLine queue1 = new QueueLine();

    @BeforeEach
    public void setUp() {
        //  Create a queue line
        queue0.setId(1L);
        queue0.setMaxSize(16);
        queue0.setShowingLetter("A");
        queue0.setTicketCounter(12);
        queue0.setTickets(List.of());

        queue0.setId(2L);
        queue0.setMaxSize(22);

        when(service.findAll()).thenReturn(List.of(queue0, queue1));
    }

    @Test
    void givenReceptionDeskIn_thenReturnTheNextTicket() throws Exception {

        mvc.perform(
                get("/staff/queueline").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].maxSize", is(queue0.getMaxSize())))
                .andExpect(jsonPath("$[1].maxSize", is(queue1.getMaxSize())));

        verify(service, times(1)).findAll();
    }
}