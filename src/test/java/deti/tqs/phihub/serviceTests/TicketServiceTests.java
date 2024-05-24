package deti.tqs.phihub.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mock.Strictness;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import deti.tqs.phihub.dtos.TicketReturnSchema;
import deti.tqs.phihub.dtos.TicketSchema;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.ReceptionDesk;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.models.WaitingRoom;
import deti.tqs.phihub.repositories.TicketRepository;
import deti.tqs.phihub.services.LastTicketsService;
import deti.tqs.phihub.services.QueueLineService;
import deti.tqs.phihub.services.ReceptionDeskService;
import deti.tqs.phihub.services.TicketService;
import deti.tqs.phihub.services.WaitingRoomService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TicketServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Mock(strictness = Strictness.LENIENT)
    private QueueLineService queueService;

    @Mock(strictness = Strictness.LENIENT)
    private WaitingRoomService waitingRoomService;

    @Mock(strictness = Strictness.LENIENT)
    private LastTicketsService lastTicketService;

    @Mock(strictness = Strictness.LENIENT)
    private ReceptionDeskService receptionDeskService;

    private Ticket ticket0 = new Ticket();
    private Ticket ticket1 = new Ticket();
    QueueLine queue0 = new QueueLine();
    QueueLine queue1 = new QueueLine();
    WaitingRoom wroom0 = new WaitingRoom();

    @BeforeEach
    public void setUp() {
        wroom0.setId(1L);
        wroom0.setNumberOfFilledSeats(8);
        wroom0.setNumberOfSeats(12);

        //  Create two tickets
        ticket0.setId(1L);
        ticket0.setPriority(true);
        ticket0.setIssueTimestamp(1L);

        ticket1.setId(2L);
        ticket1.setPriority(false);
        ticket1.setIssueTimestamp(2L);
        ticket1.setWaitingRoom(wroom0);

        queue0.setMaxSize(20);
        queue0.setShowingLetter("A");
        queue0.setTickets(new ArrayList<Ticket>(Arrays.asList(ticket0)));

        queue1.setMaxSize(10);
        queue1.setShowingLetter("P");
        queue1.setTickets(new ArrayList<Ticket>(Arrays.asList(ticket1)));

        List<Ticket> allTickets = Arrays.asList(ticket0, ticket1);

        Mockito.when(ticketRepository.save(ticket0)).thenReturn(ticket0);
        Mockito.when(ticketRepository.save(ticket1)).thenReturn(ticket1);
        Mockito.when(ticketRepository.findAll()).thenReturn(allTickets);
        Mockito.when(ticketRepository.findById(ticket0.getId())).thenReturn(Optional.of(ticket0));
        Mockito.when(ticketRepository.findById(ticket1.getId())).thenReturn(Optional.of(ticket1));
        Mockito.when(ticketRepository.findById(-99L)).thenReturn(Optional.empty());

        Mockito.when(queueService.newTicket(Mockito.any(), Mockito.eq(queue0))).thenReturn(true);
        Mockito.when(queueService.getEmptiestQueueLine()).thenReturn(queue0);
        Mockito.when(queueService.findAll()).thenReturn(List.of(queue0, queue1));

        Mockito.when(waitingRoomService.save(Mockito.any())).thenReturn(wroom0);
    }

    @Test
     void whenSaveValidTicket_thenTicketShouldBeReturned() {
        Ticket returned = ticketService.save(ticket0);
        assertThat(returned.isPriority()).isEqualTo(ticket0.isPriority());
        assertThat(returned.getIssueTimestamp()).isEqualTo(ticket0.getIssueTimestamp());

        returned = ticketService.save(ticket1);
        assertThat(returned.isPriority()).isEqualTo(ticket1.isPriority());
    }

    @Test
     void whenSearchValidID_thenTicketshouldBeFound() {
        Ticket found = ticketService.getTicketById(ticket0.getId());
        assertThat(found.isPriority()).isEqualTo(ticket0.isPriority());

        found = ticketService.getTicketById(ticket1.getId());
        assertThat(found.isPriority()).isEqualTo(ticket1.isPriority());
    }

    @Test
     void whenSearchInvalidID_thenTicketShouldNotBeFound() {
        Ticket fromDb = ticketService.getTicketById(-99L);
        assertThat(fromDb).isNull();

        Mockito.verify(ticketRepository, 
                VerificationModeFactory.times(1))
                    .findById(-99L);
    }

    @Test
     void given2Tickets_whengetAll_thenReturn2Records() {

        List<Ticket> allTickets = ticketService.findAll();

        assertThat(allTickets).hasSize(2).extracting(Ticket::isPriority).contains(ticket0.isPriority(), ticket1.isPriority());

        Mockito.verify(ticketRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }

    @Test
     void whenCreateValidTicket_thenTicketShouldBeReturned() {
        Appointment app0 = new Appointment();
        app0.setId(1L);
        app0.setPrice(12.3);
        
        TicketSchema ticketSchema = new TicketSchema(true, app0.getId());

        Mockito.when(ticketRepository.save(Mockito.any())).thenReturn(ticket0);
        Mockito.when(waitingRoomService.newTicket(wroom0)).thenReturn(true);
        Mockito.when(waitingRoomService.getEmptiestWaitingRoom()).thenReturn(wroom0);

        TicketReturnSchema returned = ticketService.createTicket(ticketSchema, app0);

        assertThat(returned.appointmentId()).isEqualTo(ticket0.getId());
    }

    @Test
     void whenCreateInvalidTicket_thenErrorShouldBeReturned() {
        Appointment app0 = new Appointment();
        app0.setId(1L);
        app0.setPrice(12.3);

        TicketSchema ticketSchema = new TicketSchema(true, app0.getId());
        
        Mockito.when(ticketRepository.save(Mockito.any())).thenReturn(ticket0);


        Mockito.when(waitingRoomService.newTicket(wroom0)).thenReturn(false);
        assertThatThrownBy(() -> ticketService.createTicket(ticketSchema, app0)).isInstanceOf(ResponseStatusException.class);

        Mockito.when(waitingRoomService.newTicket(wroom0)).thenReturn(false);
        assertThatThrownBy(() -> ticketService.createTicket(ticketSchema, app0)).isInstanceOf(ResponseStatusException.class);
        
        Mockito.when(queueService.getEmptiestQueueLine()).thenReturn(null);
        assertThatThrownBy(() -> ticketService.createTicket(ticketSchema, app0)).isInstanceOf(ResponseStatusException.class);

        Mockito.when(waitingRoomService.getEmptiestWaitingRoom()).thenReturn(null);
        assertThatThrownBy(() -> ticketService.createTicket(ticketSchema, app0)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
     void givenDeleteTicketByID_thenDeleteTicket() {

        boolean isDeleted = ticketService.deleteTicket("1");

        assertThat(isDeleted).isTrue();

        isDeleted = ticketService.deleteTicket("-99");

        assertThat(isDeleted).isFalse();
    }

    @Test
     void givenDeleteTicketByAppointmentID_thenDeleteTicket() {   
        Appointment app0 = new Appointment();
        app0.setId(1L);
        app0.setPrice(12.3);

        ticket0.setAppointment(app0);
        ticket1.setAppointment(app0);

        boolean isDeleted = ticketService.deleteTicketsByAppointmentID(app0.getId());

        assertThat(isDeleted).isTrue();
    }

    @Test
     void givenChooseNextTicket_thenChooseNextTicket() {   
        Appointment app0 = new Appointment();
        app0.setId(1L);
        app0.setPrice(12.3);

        ReceptionDesk desk0 = new ReceptionDesk();
        desk0.setId(1L);
        desk0.setServingTicket(ticket1);

        ticket0.setAppointment(app0);

        Ticket nextTicket = ticketService.getNextTicket(1);

        //  Get ticket 1 since it is in the queue "P"
        assertThat(nextTicket.getId()).isEqualTo(ticket1.getId());
    }
    
    @Test
     void givenGetNextTicketPriority_thenGetNextTicket() {   
        Appointment app0 = new Appointment();
        app0.setId(1L);
        app0.setPrice(12.3);

        ReceptionDesk desk0 = new ReceptionDesk();
        desk0.setId(1L);
        desk0.setServingTicket(ticket0);
        desk0.setDeskNumber(1);

        ticket0.setAppointment(app0);

        Ticket nextTicket = ticketService.chooseNextTicket(true, desk0.getDeskNumber());

        //  Get ticket 1 since it is in the queue "P"
        assertThat(nextTicket.getId()).isEqualTo(ticket1.getId());

        nextTicket = ticketService.chooseNextTicket(true, desk0.getDeskNumber());

        //  Get ticket 0 since it is in the queue "A"
        assertThat(nextTicket.getId()).isEqualTo(ticket0.getId());
    }
}