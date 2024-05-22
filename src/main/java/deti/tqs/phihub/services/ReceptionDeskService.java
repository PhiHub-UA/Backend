package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.repositories.ReceptionDeskRepository;
import deti.tqs.phihub.models.ReceptionDesk;
import java.util.List;
import deti.tqs.phihub.models.Ticket;

@Service
public class ReceptionDeskService {

    private ReceptionDeskRepository receptionDeskRepository;

    public ReceptionDeskService(ReceptionDeskRepository receptionDeskRepository) {
        this.receptionDeskRepository = receptionDeskRepository;
    }

    public List<ReceptionDesk> getDeskStatus() {
        return receptionDeskRepository.findAllAvailable();
    }

    public boolean updateServingTicket(int deskNumber, Ticket ticket) {

        var desk = receptionDeskRepository.findByDeskNumber(deskNumber);

        if (desk == null) {
            return false;
        }

        desk.setServingTicket(ticket);

        receptionDeskRepository.save(desk);
        
        
        return true;
    }


    
}
