package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.QueueLine;
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
    
}
