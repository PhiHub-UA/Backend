package deti.tqs.phihub.controllers.signage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.services.LastTicketsService;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/signage")
public class SignageController {

    private static final Logger logger = LoggerFactory.getLogger(SignageController.class);

    private LastTicketsService lastTicketsService;

    public SignageController(LastTicketsService lastTicketsService) {
        this.lastTicketsService = lastTicketsService;
    }

    @Operation(summary = "Get last tickets", description = "Get last tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<String> getLastAllTickets() {
        logger.info("Signage requested last tickets");
        return ResponseEntity.ok(lastTicketsService.getParsedTickets());
    }
}
