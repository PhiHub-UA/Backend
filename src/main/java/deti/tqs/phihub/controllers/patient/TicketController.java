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

@RestController
@RequestMapping("/patient/tickets")
public class TicketController {

    private AppointmentService appointmentService;
    private TicketService ticketService;


    public TicketController(AppointmentService appointmentService, TicketService ticketService) {
        this.appointmentService = appointmentService;
        this.ticketService = ticketService;

    }

    @PostMapping
    public ResponseEntity<TicketReturnSchema> createTicket(@RequestBody TicketSchema ticketSchema) {

        var appointment = appointmentService.getAppointmentById(ticketSchema.appointmentId());

        if (appointment == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Appointment not found");
        
        }

        var ticket = ticketService.createTicket(ticketSchema, appointment);

        if (ticket == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Could not create ticket");
        }

        return ResponseEntity.ok(ticket);

    }

}
