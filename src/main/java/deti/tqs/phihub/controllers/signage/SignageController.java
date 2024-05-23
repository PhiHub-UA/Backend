package deti.tqs.phihub.controllers.signage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.services.LastTicketsService;

@RestController
@RequestMapping("/signage")
public class SignageController {

    private LastTicketsService lastTicketsService;

    public SignageController(LastTicketsService lastTicketsService) {
        this.lastTicketsService = lastTicketsService;
    }

    @GetMapping
    public ResponseEntity<String> getLastAllTickets() {
        return ResponseEntity.ok(lastTicketsService.getParsedTickets());
    }
}
