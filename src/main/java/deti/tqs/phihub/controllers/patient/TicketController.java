package deti.tqs.phihub.controllers.patient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import deti.tqs.phihub.dtos.TicketReturnSchema;
import deti.tqs.phihub.dtos.TicketSchema;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.TicketService;
import org.springframework.http.ResponseEntity;
import deti.tqs.phihub.models.AppointmentState;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/patient/tickets")
public class TicketController {

    private AppointmentService appointmentService;
    private TicketService ticketService;

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);


    public TicketController(AppointmentService appointmentService, TicketService ticketService) {
        this.appointmentService = appointmentService;
        this.ticketService = ticketService;

    }
    


    @Operation(summary = "Create a ticket", description = "Create a ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Appointment referenced by ticket not found")
    })
    @PostMapping
    public ResponseEntity<TicketReturnSchema> createTicket(@RequestBody TicketSchema ticketSchema) {

        var appointment = appointmentService.getAppointmentById(ticketSchema.appointmentId());

        if (appointment == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Appointment not found");
        
        }

        appointmentService.updateAppointmentState(appointment, AppointmentState.CHECKED_IN);

        var ticket = ticketService.createTicket(ticketSchema, appointment);

        if (ticket == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Could not create ticket");
        }


        logger.info("Ticket {} created for appointment {}", ticket.id() , appointment.getId());

        return ResponseEntity.ok(ticket);

    }

}
