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

import deti.tqs.phihub.dtos.TicketSchema;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.models.WaitingRoom;
import deti.tqs.phihub.repositories.TicketRepository;
import deti.tqs.phihub.services.QueueLineService;
import deti.tqs.phihub.services.TicketService;
import deti.tqs.phihub.services.WaitingRoomService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

    private Ticket ticket0 = new Ticket();
    private Ticket ticket1 = new Ticket();

    @BeforeEach
    public void setUp() {
        //  Create two tickets
        ticket0.setId(1L);
        ticket0.setNumber(12L);
        ticket0.setPriority(true);
        ticket0.setIssueTimestamp(1L);

        ticket1.setId(2L);
        ticket1.setNumber(23L);
        ticket1.setPriority(false);
        ticket0.setIssueTimestamp(2L);

        List<Ticket> allTickets = Arrays.asList(ticket0, ticket1);

        Mockito.when(ticketRepository.save(ticket0)).thenReturn(ticket0);
        Mockito.when(ticketRepository.save(ticket1)).thenReturn(ticket1);
        Mockito.when(ticketRepository.findAll()).thenReturn(allTickets);
        Mockito.when(ticketRepository.findById(ticket0.getId())).thenReturn(Optional.of(ticket0));
        Mockito.when(ticketRepository.findById(ticket1.getId())).thenReturn(Optional.of(ticket1));
        Mockito.when(ticketRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
     void whenSaveValidTicket_thenTicketShouldBeReturned() {
        Ticket returned = ticketService.save(ticket0);
        assertThat(returned.getNumber()).isEqualTo(ticket0.getNumber());
        assertThat(returned.getIssueTimestamp()).isEqualTo(ticket0.getIssueTimestamp());

        returned = ticketService.save(ticket1);
        assertThat(returned.isPriority()).isEqualTo(ticket1.isPriority());
    }

    @Test
     void whenSearchValidID_thenTicketshouldBeFound() {
        Ticket found = ticketService.getTicketById(ticket0.getId());
        assertThat(found.getNumber()).isEqualTo(ticket0.getNumber());

        found = ticketService.getTicketById(ticket1.getId());
        assertThat(found.getNumber()).isEqualTo(ticket1.getNumber());
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

        assertThat(allTickets).hasSize(2).extracting(Ticket::getNumber).contains(ticket0.getNumber(), ticket1.getNumber());

        Mockito.verify(ticketRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }

    @Test
     void whenCreateValidTicket_thenTicketShouldBeReturned() {
        TicketSchema ticketSchema = new TicketSchema(1L, ticket0.getIssueTimestamp(), true, 1L, 1L);
        
        Appointment app0 = new Appointment();
        app0.setId(1L);
        app0.setPrice(12.3);

        QueueLine queue0 = new QueueLine();
        queue0.setMaxSize(20);
        queue0.setTickets(List.of());

        WaitingRoom wroom0 = new WaitingRoom();
        wroom0.setNumberOfFilledSeats(2);
        wroom0.setNumberOfSeats(12);
        

        Mockito.when(ticketRepository.save(Mockito.any())).thenReturn(ticket0);
        Mockito.when(queueService.newTicket(Mockito.any(), Mockito.eq(queue0))).thenReturn(true);
        Mockito.when(waitingRoomService.newTicket(wroom0)).thenReturn(true);

        Ticket returned = ticketService.createTicket(ticketSchema, app0, queue0, wroom0);

        assertThat(returned.getNumber()).isEqualTo(ticket0.getNumber());
        assertThat(returned.getIssueTimestamp()).isEqualTo(ticket0.getIssueTimestamp());
    }
}