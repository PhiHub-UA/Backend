package deti.tqs.phihub.repositoryTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.repositories.TicketRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class TicketRepositoryTests {

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket tick0 = new Ticket();
    private Ticket tick1 = new Ticket();

    @BeforeAll
    public void setUp() throws Exception {

        tick0.setId(1L);
        tick0.setQueueLetter("A");
        tick0.setNumber(53);
        
        ticketRepository.saveAndFlush(tick0);

        tick1.setId(2L);
        tick1.setQueueLetter("B");
        tick1.setNumber(89);
    }

    @Test
    void whenFindTicketById_thenReturnTicket() {

        Ticket found = ticketRepository.findById(tick0.getId()).get();
        
        assertThat(found.getQueueLetter()).isEqualTo(tick0.getQueueLetter());
        assertThat(found.getNumber()).isEqualTo(tick0.getNumber());
    }

    @Test
    void whenInvalidTicketId_thenReturnNull() {
        Ticket found = ticketRepository.findById(123L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void whenTicketIsDeleted_thenReturnNull() {
        ticketRepository.delete(tick0);

        Ticket found = ticketRepository.findById(tick0.getId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfTickets_whenFindAll_thenReturnAllTickets() {
        //  Save the ticket 1
        ticketRepository.saveAndFlush(tick1);

        List<Ticket> allTickets = ticketRepository.findAll();

        assertThat(allTickets).hasSize(2).extracting(Ticket::getQueueLetter).containsOnly(tick0.getQueueLetter(), tick1.getQueueLetter());
    }
}