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

import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.TicketRepository;
import deti.tqs.phihub.services.TicketService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TicketServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket0 = new Ticket();
    private Ticket ticket1 = new Ticket();

    @BeforeEach
    public void setUp() {
        //  Create two tickets
        ticket0.setId(1L);
        ticket0.setAddedPrice(12.3);
        ticket0.setPriority(true);
        ticket0.setRegisterDate(new Date());

        ticket1.setId(2L);
        ticket1.setAddedPrice(23.7);
        ticket1.setPriority(false);
        ticket1.setRegisterDate(new Date());

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
        assertThat(returned.getAddedPrice()).isEqualTo(ticket0.getAddedPrice());
        assertThat(returned.getRegisterDate()).isEqualTo(ticket0.getRegisterDate());

        returned = ticketService.save(ticket1);
        assertThat(returned.isPriority()).isEqualTo(ticket1.isPriority());
    }

    @Test
     void whenSearchValidID_thenTicketshouldBeFound() {
        Ticket found = ticketService.getTicketById(ticket0.getId());
        assertThat(found.getAddedPrice()).isEqualTo(ticket0.getAddedPrice());

        found = ticketService.getTicketById(ticket1.getId());
        assertThat(found.getAddedPrice()).isEqualTo(ticket1.getAddedPrice());
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

        assertThat(allTickets).hasSize(2).extracting(Ticket::getAddedPrice).contains(ticket0.getAddedPrice(), ticket1.getAddedPrice());

        Mockito.verify(ticketRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }
}
