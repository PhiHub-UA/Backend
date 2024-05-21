package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.dtos.StaffQueueSchema;
import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.QueueLineRepository;

import java.util.List;
import java.util.ArrayList;

@Service
public class QueueLineService {

    private QueueLineRepository queuelineRepository;

    public QueueLineService(QueueLineRepository queuelineRepository) {
        this.queuelineRepository = queuelineRepository;
    }

    public QueueLine save(QueueLine queueline) {
        return queuelineRepository.save(queueline);
    }

    public QueueLine getQueueLineById(Long id) {
        return queuelineRepository.findById(id).orElse(null);
    }

    public List<QueueLine> findAll() {
        return queuelineRepository.findAll();
    }

    public boolean newTicket(Ticket ticket, QueueLine queueLine) {

        if (queueLine.getTickets().size() >= queueLine.getMaxSize()) {
            return false;
        }

        queueLine.getTickets().add(ticket);
        queueLine.setTicketCounter(queueLine.getTicketCounter() + 1);
        save(queueLine);
        return true;

    }

    public QueueLine getEmptiestQueueLine() {
        List<QueueLine> queueLines = findAll();
        QueueLine emptiest = null;
        int min = Integer.MAX_VALUE;
        for (QueueLine queueLine : queueLines) {
            if (queueLine.getTickets().size() < min) {
                min = queueLine.getTickets().size();
                emptiest = queueLine;
            }
        }
        return emptiest;
    }

    public List<StaffQueueSchema> getQueueLinesWithTickets() {

        List<QueueLine> queueLines = findAll();

        List<StaffQueueSchema> staffQueueSchemas = new ArrayList<>();
    
        for (QueueLine queueLine : queueLines) {
            staffQueueSchemas.add(new StaffQueueSchema(queueLine.getId(), queueLine.getMaxSize(), queueLine.getShowingLetter(), queueLine.getTicketCounter(), queueLine.getTickets().size()));
        }


        return staffQueueSchemas;
        
    }
    
}
