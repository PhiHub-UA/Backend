package deti.tqs.phihub.controllers.staff;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import deti.tqs.phihub.models.ReceptionDesk;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.services.ReceptionDeskService;
import deti.tqs.phihub.services.TicketService;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/staff/reception")
public class ReceptionController {

    private TicketService ticketService;
    private ReceptionDeskService receptionDeskService;

    public ReceptionController(TicketService ticketService, ReceptionDeskService receptionDeskService) {
        this.ticketService = ticketService;
        this.receptionDeskService = receptionDeskService;
    }

    @Operation(summary = "Get next ticket", description = "Get next ticket for a reception desk")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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

    @Operation(summary = "Get desks status", description = "Get all operating desk status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desk status retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/desk_status")
    public ResponseEntity<List<ReceptionDesk>> getDeskStatus() {
        return ResponseEntity.ok(receptionDeskService.getDeskStatus());
    }
}
