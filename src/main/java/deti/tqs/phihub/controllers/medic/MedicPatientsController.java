package deti.tqs.phihub.controllers.medic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.User;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/medic/patients")
public class MedicPatientsController {

    private MedicService medicService;
    private AppointmentService appointmentService;

    public MedicPatientsController(MedicService medicService, AppointmentService appointmentService) {
        this.medicService = medicService;
        this.appointmentService = appointmentService;

    }

    @Operation(summary = "Get medic patients", description = "Get medic patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<Set<User>> getMedicPatients() {

        var medic = medicService.getMedicFromContext();

        if (medic == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        List<Appointment> appointments = appointmentService.getAppointmentsByMedic(medic);

        Set<User> patients = new HashSet<>();

        for (Appointment appointment : appointments) {
            patients.add(appointment.getPatient());
        }

        return ResponseEntity.ok(patients);

    }

}
