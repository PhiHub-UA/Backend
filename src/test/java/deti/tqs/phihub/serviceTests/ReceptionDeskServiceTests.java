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

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.ReceptionDesk;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.ReceptionDeskRepository;
import deti.tqs.phihub.services.ReceptionDeskService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReceptionDeskServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private ReceptionDeskRepository receptionDeskRepository;

    @InjectMocks
    private ReceptionDeskService deskService;

    private ReceptionDesk desk0 = new ReceptionDesk();
    private ReceptionDesk desk1 = new ReceptionDesk();
    private Ticket ticket0 = new Ticket();
    private Appointment app0 = new Appointment();


    @BeforeEach
    public void setUp() {

        //  Create a appointment
        app0.setId(1L);
        app0.setPrice(12.3);

        //  Create a ticket
        ticket0.setId(1L);
        ticket0.setIssueTimestamp(1L);
        ticket0.setPriority(false);
        ticket0.setAppointment(app0);

        //  Create two desks
        desk0.setId(1L);
        desk0.setDeskNumber(3);
        desk0.setServingTicket(ticket0);
        desk0.setWorking(true);

        desk1.setId(2L);
        desk1.setDeskNumber(5);
        desk1.setServingTicket(null);
        desk1.setWorking(false);


        Mockito.when(receptionDeskRepository.save(Mockito.any())).thenReturn(desk0);
        Mockito.when(receptionDeskRepository.findAll()).thenReturn(List.of(desk0, desk1));
        Mockito.when(receptionDeskRepository.findAllAvailable()).thenReturn(List.of(desk0));
        Mockito.when(receptionDeskRepository.findByDeskNumber(desk0.getDeskNumber())).thenReturn(desk0);
    }

    @Test
     void whenGetValidDeskStatus_thenTrueShouldBeReturned() {
        List<ReceptionDesk> returned = deskService.getDeskStatus();
        assertThat(returned.get(0).getDeskNumber()).isEqualTo(desk0.getDeskNumber());
    }

    @Test
     void whenSearchValidID_thenDeskshouldBeFound() {
        deskService.deleteTicketsByAppointmentID(ticket0.getId());

        Mockito.verify(receptionDeskRepository, 
                VerificationModeFactory.times(1))
                    .save(Mockito.any());
    }

    @Test
     void whenSearchInvalidID_thenDeskShouldNotBeFound() {
        boolean saved = deskService.updateServingTicket(desk0.getDeskNumber(), ticket0);
        assertThat(saved).isTrue();

        Mockito.verify(receptionDeskRepository, 
                VerificationModeFactory.times(1))
                .save(Mockito.any());
    } 

    @Test
     void whenSearchValidIDandTimeStamp_thenDeskAvailabilityShouldBeFound() {
        boolean saved = deskService.updateServingTicket(99, ticket0);
        assertThat(saved).isFalse();

        Mockito.verify(receptionDeskRepository, 
                VerificationModeFactory.times(0))
                .save(Mockito.any());
    }
}