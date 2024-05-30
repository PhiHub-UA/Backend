package deti.tqs.phihub.controllers.medic;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Medic;


import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/medic")
public class MedicController {

    private static final Logger logger = LoggerFactory.getLogger(MedicController.class);

    private AppointmentService appointmentService;
    private MedicService medicService;

    @Autowired
    public MedicController(AppointmentService appointmentService, MedicService medicService) {
        this.appointmentService = appointmentService;
        this.medicService = medicService;
    }


    @Operation(summary = "Get medic appointments", description = "Get medic appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getMedicAppointments() {

        Medic medic = medicService.getMedicFromContext();

        if (medic == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        }

        logger.info("Medic {} requested his/her appointments", medic.getName());

        return ResponseEntity.ok(appointmentService.getAppointmentsByMedic(medic));
    
    }


    
}
