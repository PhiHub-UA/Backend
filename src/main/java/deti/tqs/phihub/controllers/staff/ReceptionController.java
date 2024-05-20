package deti.tqs.phihub.controllers.staff;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.services.TicketService;


@RestController
@RequestMapping("/staff/reception")
public class ReceptionController {

    private TicketService ticketService;


    public ReceptionController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @GetMapping("/next/{queueLineId}")
    public ResponseEntity<Ticket> getNextTicket(@PathVariable("queueLineId") Long queueLineId){

        return ResponseEntity.ok(ticketService.getNextTicket(queueLineId));

    }
    
}
