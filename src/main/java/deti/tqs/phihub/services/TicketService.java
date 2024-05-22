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
    private ReceptionDeskService receptionDeskService;

    public TicketService(TicketRepository ticketRepository, QueueLineService queueLineService,
            WaitingRoomService waitingRoomService, ReceptionDeskService receptionDeskService) {
        this.ticketRepository = ticketRepository;
        this.queueLineService = queueLineService;
        this.waitingRoomService = waitingRoomService;
        this.receptionDeskService = receptionDeskService;
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

    public boolean deleteTicket(String ticketID) {

        Ticket ticket = getTicketById(Long.parseLong(ticketID));

        if (ticket == null) {
            return false;
        }

        ticketRepository.delete(ticket);

        return true;
    }

    public void deleteTicketsByAppointmentID(Long appointmentID) {

        List<Ticket> tickets = findAll();

        for (Ticket t : tickets) {
            if (t.getAppointment().getId() == appointmentID) {
                ticketRepository.delete(t);
            }
        }

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
        ticket.setTicketName(queueLine.getShowingLetter()+queueLine.getTicketCounter());

        save(ticket);

        if (!queueLineService.newTicket(ticket, queueLine)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Could not register ticket in queue line");
        }

        if (!waitingRoomService.newTicket(waitingRoom)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Could not register ticket in waiting room");
        }

        return new TicketReturnSchema(ticket.getId(), ticket.getAppointment().getId(),
                queueLine.getShowingLetter(),queueLine.getTicketCounter(), waitingRoom.getId());
    }

    public Ticket getNextTicket( int deskNumber ) {


        Ticket nextTicket = chooseNextTicket();

        if (nextTicket == null) {
            return null;
        }

        // remove 1 from waiting room

        WaitingRoom waitingRoom = nextTicket.getWaitingRoom();

        if (waitingRoom == null) {
            return null;
        }

        waitingRoom.setNumberOfFilledSeats(waitingRoom.getNumberOfFilledSeats() - 1);
        waitingRoomService.save(waitingRoom);

        receptionDeskService.updateServingTicket(deskNumber, nextTicket);

        return nextTicket;
    }


    public Ticket chooseNextTicket() {

       // find all queuelines, check if queueline P has any tickets ( since its priority ) and get the first one 

        Ticket nextTicket = null;

        List<QueueLine> queueLine = queueLineService.findAll();


        // if queueline with letter P has any ticket, return the first one 

        for (QueueLine q : queueLine) {
            if (q.getShowingLetter().equals("P") && q.getTickets().size() > 0) {
                nextTicket = q.getTickets().remove(0);
                queueLineService.save(q);
                return nextTicket;
            }
        }

        // now check all the other queuelines and give the first ticket of the one that has the most tickets

        int max = 0;
        QueueLine maxQueueLine = null;

        for (QueueLine q : queueLine) {
            if (q.getTickets().size() > max) {
                max = q.getTickets().size();
                maxQueueLine = q;
            }
        }

        if (maxQueueLine == null) {
            return null;
        }

        nextTicket = maxQueueLine.getTickets().remove(0);
        queueLineService.save(maxQueueLine);

        return nextTicket;

    }


}
