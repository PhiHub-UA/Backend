package deti.tqs.phihub.repositoryTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.phihub.models.WaitingRoom;
import deti.tqs.phihub.repositories.WaitingRoomRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class WaitingRoomRepositoryTests {

    @Autowired
    private WaitingRoomRepository waitingroomRepository;

    private WaitingRoom wroom0 = new WaitingRoom();
    private WaitingRoom wroom1 = new WaitingRoom();

    @BeforeAll
    public void setUp() {

        wroom0.setId(1L);
        wroom0.setName("Room A");
        wroom0.setNumberOfFilledSeats(7);
        wroom0.setNumberOfSeats(20);
        
        waitingroomRepository.saveAndFlush(wroom0);

        wroom1.setId(2L);
        wroom1.setName("Room R");
        wroom1.setNumberOfSeats(34);
    }

    @Test
    void whenFindWaitingRoomById_thenReturnWaitingRoom() {

        WaitingRoom found = waitingroomRepository.findById(wroom0.getId()).get();
        
        assertThat(found.getName()).isEqualTo(wroom0.getName());
        assertThat(found.getNumberOfFilledSeats()).isEqualTo(wroom0.getNumberOfFilledSeats());
        assertThat(found.getNumberOfSeats()).isEqualTo(wroom0.getNumberOfSeats());
    }

    @Test
    void whenInvalidWaitingRoomId_thenReturnNull() {
        WaitingRoom found = waitingroomRepository.findById(123L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void whenWaitingRoomIsDeleted_thenReturnNull() {
        waitingroomRepository.delete(wroom0);

        WaitingRoom found = waitingroomRepository.findById(wroom0.getId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfWaitingRooms_whenFindAll_thenReturnAllWaitingRooms() {
        //  Save the waitingroom 1
        waitingroomRepository.saveAndFlush(wroom1);

        List<WaitingRoom> allWaitingRooms = waitingroomRepository.findAll();

        assertThat(allWaitingRooms).hasSize(2).extracting(WaitingRoom::getName).containsOnly(wroom0.getName(), wroom1.getName());
    }
}