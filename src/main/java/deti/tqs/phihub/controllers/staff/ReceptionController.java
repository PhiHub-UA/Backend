package deti.tqs.phihub.controllers.staff;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.services.TicketService;

@RestController
@RequestMapping("/staff/reception")
public class ReceptionController {

    private TicketService ticketService;

    public ReceptionController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/next")
    public ResponseEntity<Ticket> getNextTicket() {

        Ticket nextTicket = ticketService.getNextTicket();

        if (nextTicket == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "No tickets available");
        }

        return ResponseEntity.ok(nextTicket);

    }

}
