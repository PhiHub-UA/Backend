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
import deti.tqs.phihub.controllers.staff.ReceptionController;
import deti.tqs.phihub.dtos.MedicSchema;
import deti.tqs.phihub.models.ReceptionDesk;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.models.StaffPermissions;
import deti.tqs.phihub.models.Ticket;
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

@WebMvcTest(ReceptionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReceptionControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReceptionDeskService service;

    @MockBean
    private StaffService staffService;
    @MockBean
    private TicketService ticketService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private Staff staff0 = new Staff();
    private Ticket ticket0 = new Ticket();
    private ReceptionDesk desk0 = new ReceptionDesk();
    MedicSchema medicSchema;

    @BeforeEach
    public void setUp() {
        //  Create a ticket
        ticket0.setId(1L);
        ticket0.setIssueTimestamp(1L);
        ticket0.setPriority(false);

        //  Create a reception desk
        desk0.setId(1L);
        desk0.setDeskNumber(1);
        desk0.setServingTicket(ticket0);
        desk0.setWorking(true);

        //  Create a staff
        staff0.setId(1L);
        staff0.setUsername("josefino");
        staff0.setEmail("jose@fino.com");
        staff0.setPhone("919828737");
        staff0.setPermissions(List.of(StaffPermissions.CREATE));

        when(service.getDeskStatus()).thenReturn(List.of(desk0));
        when(ticketService.getNextTicket(1)).thenReturn(ticket0);
    }

    @Test
    void givenReceptionDeskIn_thenReturnTheNextTicket() throws Exception {

        mvc.perform(
                get("/staff/reception/next/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issueTimestamp", is(ticket0.getIssueTimestamp().intValue())));

        verify(ticketService, times(1)).getNextTicket(1);
    }

    @Test
    void givenNoReceptionDeskIn_thenReturnError() throws Exception {
        when(ticketService.getNextTicket(1)).thenReturn(null);

        mvc.perform(
                get("/staff/reception/next/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(ticketService, times(1)).getNextTicket(1);
    }

    @Test
    void getDeskStatus_thenDeskStatus() throws Exception {
        mvc.perform(
                get("/staff/reception/desk_status").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deskNumber", is(desk0.getDeskNumber())))
                .andExpect(jsonPath("$[0].servingTicket.issueTimestamp", is(ticket0.getIssueTimestamp().intValue())));
    }
}