package deti.tqs.phihub.controllers.patient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.dtos.TicketSchema;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.QueueLineService;
import deti.tqs.phihub.services.TicketService;
import deti.tqs.phihub.services.WaitingRoomService;
import deti.tqs.phihub.models.Ticket;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/patient/tickets")
public class TicketController {

    private AppointmentService appointmentService;
    private QueueLineService queueLineService;
    private TicketService ticketService;
    private WaitingRoomService waitingRoomService;

    public TicketController(AppointmentService appointmentService, QueueLineService queueLineService,
            TicketService ticketService, WaitingRoomService waitingRoomService) {
        this.appointmentService = appointmentService;
        this.queueLineService = queueLineService;
        this.ticketService = ticketService;
        this.waitingRoomService = waitingRoomService;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketSchema ticketSchema) {

        var appointment = appointmentService.getAppointmentById(ticketSchema.appointmentId());

        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        var queueLine = queueLineService.getQueueLineById(ticketSchema.queueLineId());

        if (queueLine == null) {
            return ResponseEntity.notFound().build();
        }

        var waitingRoom = waitingRoomService.getWaitingRoomById(ticketSchema.waitingRoomId());

        if (waitingRoom == null) {
            return ResponseEntity.notFound().build();
        }

        var ticket = ticketService.createTicket(ticketSchema, appointment, queueLine, waitingRoom);

        if (ticket == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(ticket);

    }

}
