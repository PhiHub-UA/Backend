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

import deti.tqs.phihub.models.LastTickets;
import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.LastTicketsRepository;
import deti.tqs.phihub.services.LastTicketsService;
import deti.tqs.phihub.services.QueueLineService;
import deti.tqs.phihub.services.TicketService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LastTicketsServiceTests {

    @InjectMocks
    private LastTicketsService lastTicketsService;

    @Mock(strictness = Strictness.LENIENT)
    private LastTicketsRepository lastTicketsRepository;

    @Mock(strictness = Strictness.LENIENT)
    private QueueLineService queueLineService;

    private LastTickets lastTickets0 = new LastTickets();
    private Ticket ticket0 = new Ticket();
    private Ticket ticket1 = new Ticket();
    QueueLine queue0 = new QueueLine();
    QueueLine queue1 = new QueueLine();

    @BeforeEach
    public void setUp() {
        //  Create a last tickets array
        lastTickets0.setId(1L);
        lastTickets0.setLastTicketQueue(new ArrayList<String>());
        lastTickets0.getLastTicketQueue().add("[\"P0\", 1]");

        //  Create two tickets
        ticket0.setId(1L);
        ticket0.setPriority(true);
        ticket0.setIssueTimestamp(1L);
        ticket0.setTicketName("A0");

        ticket1.setId(2L);
        ticket1.setPriority(false);
        ticket1.setIssueTimestamp(2L);
        ticket1.setTicketName("A1");

        //  Create two queues
        queue0.setMaxSize(20);
        queue0.setShowingLetter("A");
        queue0.setTickets(new ArrayList<Ticket>(Arrays.asList(ticket0)));

        queue1.setMaxSize(10);
        queue1.setShowingLetter("P");
        queue1.setTickets(new ArrayList<Ticket>(Arrays.asList(ticket1)));

        Mockito.when(lastTicketsRepository.save(lastTickets0)).thenReturn(lastTickets0);
        Mockito.when(lastTicketsRepository.findAll()).thenReturn(List.of(lastTickets0));
        Mockito.when(queueLineService.findAll()).thenReturn(List.of(queue0, queue1));
    }

    @Test
     void whenSaveValidLastTickets_thenLastTicketsShouldBeReturned() {
        LastTickets returned = lastTicketsService.save(lastTickets0);
        assertThat(returned.getId()).isEqualTo(lastTickets0.getId());
        assertThat(returned.getLastTicketQueue().get(0)).isEqualTo(lastTickets0.getLastTicketQueue().get(0));
    }

    @Test
     void givenLastTickets_whengetAllTicktets_thenReturnOne() {
        LastTickets firstLastTickets = lastTicketsService.findTickets();
        assertThat(firstLastTickets.getLastTicketQueue().get(0)).isEqualTo(lastTickets0.getLastTicketQueue().get(0));

        Mockito.verify(lastTicketsRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }

    @Test
     void whenAddNewTickets_thenNewTicketsShouldBeReturned() {
        LastTickets firstLastTickets = lastTicketsService.addNewTicket(ticket0, 3);
        assertThat(firstLastTickets.getLastTicketQueue().get(1)).isEqualTo("[\"" + ticket0.getTicketName() + "\", 3]");

        Mockito.verify(lastTicketsRepository, 
                VerificationModeFactory.times(1))
                    .save(Mockito.any());
    }

    @Test
     void givenLastTickets_whengetParsedLastTicktets_thenReturnAll() {
        String parsedLastTickets = lastTicketsService.getParsedTickets();

        assertThat(parsedLastTickets).isEqualTo("{\"1\": [\"P0\", 1], \"nextnum\": \"A1\"}");

        Mockito.verify(lastTicketsRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }

    @Test
     void givenNewTickets_whengetchooseNextTicket_thenGetPriority() {
        //  Ticket 1 is returned first since it was from the Priority Queue line
        Ticket nextTicket = lastTicketsService.chooseNextTicket();
        assertThat(nextTicket.getTicketName()).isEqualTo(ticket1.getTicketName());
        
        Mockito.when(queueLineService.findAll()).thenReturn(List.of(queue0));

        nextTicket = lastTicketsService.chooseNextTicket();
        assertThat(nextTicket.getTicketName()).isEqualTo(ticket0.getTicketName());

        Mockito.verify(queueLineService, 
                VerificationModeFactory.times(2))
                    .findAll();
    }
}