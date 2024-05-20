package deti.tqs.phihub.controllers.staff;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import deti.tqs.phihub.models.QueueLine;
import java.util.List;


import deti.tqs.phihub.services.QueueLineService;

@RequestMapping("/staff/queueline")
@RestController
public class QueueLineController {


    private QueueLineService queueLineService;

    public QueueLineController(QueueLineService queueLineService) {
        this.queueLineService = queueLineService;
    }

    @GetMapping
    public ResponseEntity<List<QueueLine>> getAllQueueLines() {
        return ResponseEntity.ok(queueLineService.findAll());
    }

    
}
