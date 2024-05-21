package deti.tqs.phihub.defaults;

import org.springframework.boot.ApplicationRunner;

import deti.tqs.phihub.configs.Generated;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;


import deti.tqs.phihub.models.WaitingRoom;
import deti.tqs.phihub.repositories.WaitingRoomRepository;

@Component
@Generated
public class WaitingRooms implements ApplicationRunner {

    private WaitingRoomRepository waitingRoomRepository;

    public WaitingRooms(WaitingRoomRepository waitingRoomRepository) {
        this.waitingRoomRepository = waitingRoomRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (waitingRoomRepository.count() > 0) {
            return;
        }

        WaitingRoom waitingRoom1 = new WaitingRoom();
        waitingRoom1.setName("Wait Room 2");
        waitingRoom1.setNumberOfSeats(20);
        waitingRoom1.setNumberOfFilledSeats(0);

        WaitingRoom waitingRoom2 = new WaitingRoom();
        waitingRoom2.setName("Wait Room 2");
        waitingRoom2.setNumberOfSeats(10);
        waitingRoom2.setNumberOfFilledSeats(0);

        waitingRoomRepository.save(waitingRoom1);
        waitingRoomRepository.save(waitingRoom2);
    }
}
