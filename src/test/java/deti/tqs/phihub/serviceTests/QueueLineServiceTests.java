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

import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.QueueLineRepository;
import deti.tqs.phihub.services.QueueLineService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class QueueLineServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private QueueLineRepository queuelineRepository;

    @InjectMocks
    private QueueLineService queuelineService;

    private QueueLine queueline0 = new QueueLine();
    private QueueLine queueline1 = new QueueLine();

    private Ticket tick0 = new Ticket();
    private Ticket tick1 = new Ticket();

    @BeforeEach
    public void setUp() {
        tick0.setPriority(false);
        tick1.setPriority(true);

        //  Create two queuelines
        queueline0.setId(1L);
        queueline0.setMaxSize(17);
        queueline0.setTickets(Arrays.asList(tick0));

        queueline1.setId(2L);
        queueline1.setMaxSize(13);
        queueline1.setTickets(Arrays.asList(tick0, tick1));

        List<QueueLine> allQueueLines = Arrays.asList(queueline0, queueline1);

        Mockito.when(queuelineRepository.save(queueline0)).thenReturn(queueline0);
        Mockito.when(queuelineRepository.save(queueline1)).thenReturn(queueline1);
        Mockito.when(queuelineRepository.findAll()).thenReturn(allQueueLines);
        Mockito.when(queuelineRepository.findById(queueline0.getId())).thenReturn(Optional.of(queueline0));
        Mockito.when(queuelineRepository.findById(queueline1.getId())).thenReturn(Optional.of(queueline1));
        Mockito.when(queuelineRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
     void whenSaveValidQueueLine_thenQueueLineShouldBeReturned() {
        QueueLine returned = queuelineService.save(queueline0);
        assertThat(returned.getMaxSize()).isEqualTo(queueline0.getMaxSize());

        returned = queuelineService.save(queueline1);
        assertThat(returned.getMaxSize()).isEqualTo(queueline1.getMaxSize());
    }

    @Test
     void whenSearchValidID_thenQueueLineshouldBeFound() {
        QueueLine found = queuelineService.getQueueLineById(queueline0.getId());
        assertThat(found.getMaxSize()).isEqualTo(queueline0.getMaxSize());

        found = queuelineService.getQueueLineById(queueline1.getId());
        assertThat(found.getMaxSize()).isEqualTo(queueline1.getMaxSize());
    }

    @Test
     void whenSearchInvalidID_thenQueueLineShouldNotBeFound() {
        QueueLine fromDb = queuelineService.getQueueLineById(-99L);
        assertThat(fromDb).isNull();

        Mockito.verify(queuelineRepository, 
                VerificationModeFactory.times(1))
                    .findById(-99L);
    }

    @Test
     void given2QueueLines_whengetAll_thenReturn2Records() {

        List<QueueLine> allQueueLines = queuelineService.findAll();

        assertThat(allQueueLines).hasSize(2).extracting(QueueLine::getTickets).contains(queueline0.getTickets(), queueline1.getTickets());

        Mockito.verify(queuelineRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }

    @Test
     void whenCreateValidTicket_thenTicketShouldBeReturned() {
        QueueLine queue0 = new QueueLine();
        queue0.setMaxSize(20);
        queue0.setTickets(new ArrayList<Ticket>(Arrays.asList()));

        boolean returned = queuelineService.newTicket(tick0, queue0);

        assertThat(returned).isTrue();

        Mockito.verify(queuelineRepository, 
                VerificationModeFactory.times(1))
                    .save(Mockito.any());
    }
}