package deti.tqs.phihub.controllers.staff;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import deti.tqs.phihub.models.QueueLine;
import java.util.List;


import deti.tqs.phihub.services.QueueLineService;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/staff/queueline")
@RestController
public class QueueLineController {

    private static final Logger logger = LoggerFactory.getLogger(QueueLineController.class);

    private QueueLineService queueLineService;

    public QueueLineController(QueueLineService queueLineService) {
        this.queueLineService = queueLineService;
    }

    @Operation(summary = "Get all queue lines", description = "Get all queue lines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue lines retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<QueueLine>> getAllQueueLines() {
        logger.info("Staff requested all queue lines");
        return ResponseEntity.ok(queueLineService.findAll());
    }


    
}
