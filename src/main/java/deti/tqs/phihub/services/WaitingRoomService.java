package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.WaitingRoom;
import deti.tqs.phihub.repositories.WaitingRoomRepository;

import java.util.List;

@Service
public class WaitingRoomService {

    private WaitingRoomRepository waitingroomRepository;

    public WaitingRoomService(WaitingRoomRepository waitingroomRepository) {
        this.waitingroomRepository = waitingroomRepository;
    }

    public WaitingRoom save(WaitingRoom waitingroom) {
        return waitingroomRepository.save(waitingroom);
    }

    public WaitingRoom getWaitingRoomById(Long id) {
        return waitingroomRepository.findById(id).orElse(null);
    }

    public List<WaitingRoom> findAll() {
        return waitingroomRepository.findAll();
    }


    public boolean newTicket( WaitingRoom waitingRoom) {

        if (waitingRoom.getNumberOfFilledSeats() < waitingRoom.getNumberOfSeats()) {
            waitingRoom.setNumberOfFilledSeats(waitingRoom.getNumberOfFilledSeats() + 1);
            save(waitingRoom);
            return true;
        }
        else {
            return false;
        }
        
    }

    public WaitingRoom getEmptiestWaitingRoom() {
        List<WaitingRoom> waitingRooms = findAll();
        WaitingRoom emptiest = null;
        int min = Integer.MAX_VALUE;
        for (WaitingRoom waitingRoom : waitingRooms) {
            if (waitingRoom.getNumberOfFilledSeats() < min) {
                min = waitingRoom.getNumberOfFilledSeats();
                emptiest = waitingRoom;
            }
        }
        return emptiest;
    }
    
}
