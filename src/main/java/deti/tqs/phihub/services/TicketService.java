package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.dtos.TicketReturnSchema;
import deti.tqs.phihub.dtos.TicketSchema;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.TicketRepository;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.WaitingRoom;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TicketService {

    private TicketRepository ticketRepository;
    private QueueLineService queueLineService;
    private WaitingRoomService waitingRoomService;

    public TicketService(TicketRepository ticketRepository, QueueLineService queueLineService,
            WaitingRoomService waitingRoomService) {
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

    public TicketReturnSchema createTicket(TicketSchema ticketSchema, Appointment appointment) {

        var ticket = new Ticket();
        ticket.setAppointment(appointment);

        // get the emptiest queue line

        QueueLine queueLine = queueLineService.getEmptiestQueueLine();

        if (queueLine == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "No queue lines available");

        }

        // get emptiest waiting room

        WaitingRoom waitingRoom = waitingRoomService.getEmptiestWaitingRoom();

        if (waitingRoom == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "No waiting rooms available");
        }

        ticket.setWaitingRoom(waitingRoom);

        ticket.setIssueTimestamp(System.currentTimeMillis());
        ticket.setPriority(ticketSchema.priority());

        save(ticket);

        if (!queueLineService.newTicket(ticket, queueLine)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Could not register ticket in queue line");
        }

        if (!waitingRoomService.newTicket(waitingRoom)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Could not register ticket in waiting room");
        }

        TicketReturnSchema ticketReturnSchema = new TicketReturnSchema(ticket.getId(), ticket.getAppointment().getId(),
                queueLine.getShowingLetter(),queueLine.getTicketCounter(), waitingRoom.getId());

        return ticketReturnSchema;
    }

    public Ticket getNextTicket(Long queueLineId) {

        QueueLine queueLine = queueLineService.getQueueLineById(queueLineId);
        if (queueLine == null) {
            return null;
        }

        List<Ticket> tickets = queueLine.getTickets();
        if (tickets.isEmpty()) {
            return null;
        }

        Ticket ticket = tickets.get(0);
        tickets.remove(0);
        queueLine.setTickets(tickets);
        queueLineService.save(queueLine);

        // remove 1 from waiting room

        WaitingRoom waitingRoom = ticket.getWaitingRoom();

        if (waitingRoom == null) {
            return null;
        }

        waitingRoom.setNumberOfFilledSeats(waitingRoom.getNumberOfFilledSeats() - 1);
        waitingRoomService.save(waitingRoom);

        return ticket;
    }

}
