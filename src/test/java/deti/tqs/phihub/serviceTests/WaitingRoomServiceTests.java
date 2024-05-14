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

import deti.tqs.phihub.models.WaitingRoom;
import deti.tqs.phihub.repositories.WaitingRoomRepository;
import deti.tqs.phihub.services.WaitingRoomService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WaitingRoomServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private WaitingRoomRepository waitingroomRepository;

    @InjectMocks
    private WaitingRoomService waitingroomService;

    private WaitingRoom waitingroom0 = new WaitingRoom();
    private WaitingRoom waitingroom1 = new WaitingRoom();

    @BeforeEach
    public void setUp() {
        //  Create two waitingrooms
        waitingroom0.setId(1L);
        waitingroom0.setName("Room A");
        waitingroom1.setNumberOfSeats(14);
        waitingroom0.setNumberOfFilledSeats(7);

        waitingroom1.setId(2L);
        waitingroom1.setName("Room R");
        waitingroom1.setNumberOfSeats(20);
        waitingroom1.setNumberOfFilledSeats(16);

        List<WaitingRoom> allWaitingRooms = Arrays.asList(waitingroom0, waitingroom1);

        Mockito.when(waitingroomRepository.save(waitingroom0)).thenReturn(waitingroom0);
        Mockito.when(waitingroomRepository.save(waitingroom1)).thenReturn(waitingroom1);
        Mockito.when(waitingroomRepository.findAll()).thenReturn(allWaitingRooms);
        Mockito.when(waitingroomRepository.findById(waitingroom0.getId())).thenReturn(Optional.of(waitingroom0));
        Mockito.when(waitingroomRepository.findById(waitingroom1.getId())).thenReturn(Optional.of(waitingroom1));
        Mockito.when(waitingroomRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
     void whenSaveValidWaitingRoom_thenWaitingRoomShouldBeReturned() {
        WaitingRoom returned = waitingroomService.save(waitingroom0);
        assertThat(returned.getName()).isEqualTo(waitingroom0.getName());

        returned = waitingroomService.save(waitingroom1);
        assertThat(returned.getName()).isEqualTo(waitingroom1.getName());
    }

    @Test
     void whenSearchValidID_thenWaitingRoomshouldBeFound() {
        WaitingRoom found = waitingroomService.getWaitingRoomById(waitingroom0.getId());
        assertThat(found.getName()).isEqualTo(waitingroom0.getName());

        found = waitingroomService.getWaitingRoomById(waitingroom1.getId());
        assertThat(found.getName()).isEqualTo(waitingroom1.getName());
    }

    @Test
     void whenSearchInvalidID_thenWaitingRoomShouldNotBeFound() {
        WaitingRoom fromDb = waitingroomService.getWaitingRoomById(-99L);
        assertThat(fromDb).isNull();

        Mockito.verify(waitingroomRepository, 
                VerificationModeFactory.times(1))
                    .findById(-99L);
    }

    @Test
     void given2WaitingRooms_whengetAll_thenReturn2Records() {

        List<WaitingRoom> allWaitingRooms = waitingroomService.findAll();

        assertThat(allWaitingRooms).hasSize(2).extracting(WaitingRoom::getNumberOfFilledSeats).contains(waitingroom0.getNumberOfFilledSeats(), waitingroom1.getNumberOfFilledSeats());

        Mockito.verify(waitingroomRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }
}
