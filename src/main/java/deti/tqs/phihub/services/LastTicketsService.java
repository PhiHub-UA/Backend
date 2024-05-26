package deti.tqs.phihub.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.LastTicketsRepository;
import deti.tqs.phihub.models.LastTickets;
import deti.tqs.phihub.models.QueueLine;

@Service
public class LastTicketsService {

    private LastTicketsRepository lastTicketsRepository;
    private QueueLineService queueLineService;

    public LastTicketsService(LastTicketsRepository lastTicketsRepository, QueueLineService queueLineService) {
        this.lastTicketsRepository = lastTicketsRepository;
        this.queueLineService = queueLineService;
    }

    public LastTickets save(LastTickets ticket) {
        return lastTicketsRepository.save(ticket);
    }

    public LastTickets findTickets() {
        List<LastTickets> lastTickets = lastTicketsRepository.findAll();

        if (lastTickets.isEmpty()) {
            return null;
        }
        else {
            return lastTickets.get(0);
        }
    }

    public LastTickets addNewTicket(Ticket ticket, int deskNumber) {
        LastTickets lastTicketsObj = findTickets();

        if (lastTicketsObj == null) {
            lastTicketsObj = new LastTickets();
            lastTicketsObj.setLastTicketQueue(new ArrayList<>(Arrays.asList()));
        }

        if (lastTicketsObj.getLastTicketQueue().size() > 4) {
            lastTicketsObj.getLastTicketQueue().remove(0);
        }

        lastTicketsObj.getLastTicketQueue().add("[\"" + ticket.getTicketName() + "\", " + deskNumber + "]");

        save(lastTicketsObj);

        return lastTicketsObj;

    }

    public String getParsedTickets() {
        LastTickets lastTicketsObj = findTickets();
        
        StringBuilder finalStr = new StringBuilder();
        finalStr.append("{");

        if (lastTicketsObj != null) {
        
            List<String> ticketList = lastTicketsObj.getLastTicketQueue();
    
            Integer currNum = 1;
    
            for (String ticket : ticketList) {
                finalStr.append("\"" + currNum + "\": " + ticket + ", ");
                currNum += 1;
            }
        }

        Ticket nextCall = chooseNextTicket();

        if (nextCall != null) {
            finalStr.append("\"nextnum\": \"" + nextCall.getTicketName() + "\"");
        }
        else {
            finalStr.append("\"nextnum\": \"---\"");
        }

        finalStr.append("}");

        return finalStr.toString();
    }
    
    public Ticket chooseNextTicket() {

       // find all queuelines, check if queueline P has any tickets ( since its priority ) and get the first one 

        Ticket nextTicket = null;

        List<QueueLine> queueLine = queueLineService.findAll();


        // if queueline with letter P has any ticket, return the first one 

        for (QueueLine q : queueLine) {
            if (q.getShowingLetter().equals("P") && q.getTickets().size() > 0) {
                nextTicket = q.getTickets().get(0);
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
        nextTicket = maxQueueLine.getTickets().get(0);

        return nextTicket;
    }
}
