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
    
}
