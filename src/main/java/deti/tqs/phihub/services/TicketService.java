package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.dtos.TicketSchema;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.TicketRepository;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.WaitingRoom;

import java.util.List;

@Service
public class TicketService {

    private TicketRepository ticketRepository;
    private QueueLineService queueLineService;
    private WaitingRoomService waitingRoomService;

    public TicketService(TicketRepository ticketRepository, QueueLineService queueLineService, WaitingRoomService waitingRoomService) {
        this.ticketRepository = ticketRepository;
        this.queueLineService = queueLineService;
        this.waitingRoomService = waitingRoomService;
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket createTicket(TicketSchema ticketSchema, Appointment appointment, QueueLine queueLine, WaitingRoom waitingRoom) {

        var ticket = new Ticket();
        ticket.setAppointment(appointment);
        ticket.setWaitingRoom(waitingRoom);
        ticket.setIssueTimestamp(ticketSchema.issueTimestamp());
        ticket.setPriority(ticketSchema.priority());
        ticket.setNumber(ticketRepository.count() + 1);

        Ticket ticketSaved = save(ticket);

        if (!queueLineService.newTicket(ticket, queueLine)) {
            return null;
        }

        if (!waitingRoomService.newTicket(waitingRoom)) {
            return null;
        }

        return ticketSaved;
    }
    
}
