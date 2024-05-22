package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.QueueLine;
import deti.tqs.phihub.models.Ticket;
import deti.tqs.phihub.repositories.QueueLineRepository;

import java.util.List;

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



}
