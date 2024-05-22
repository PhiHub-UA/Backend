package deti.tqs.phihub.controllers.staff;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.services.ReceptionDeskService;
import deti.tqs.phihub.services.TicketService;

@RestController
@RequestMapping("/staff/reception")
public class ReceptionController {

    private TicketService ticketService;
    private ReceptionDeskService receptionDeskService;

    public ReceptionController(TicketService ticketService, ReceptionDeskService receptionDeskService) {
        this.ticketService = ticketService;
        this.receptionDeskService = receptionDeskService;
    }

    @GetMapping("/next/{counterNumber}")
    public ResponseEntity<Ticket> getNextTicket(@PathVariable int counterNumber)
     {

        Ticket nextTicket = ticketService.getNextTicket(counterNumber);

        if (nextTicket == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "No tickets available");
        }

        return ResponseEntity.ok(nextTicket);

    }

    @GetMapping("/desk_status")
    public ResponseEntity<?> getDeskStatus() {
        return ResponseEntity.ok(receptionDeskService.getDeskStatus());
    }

}
