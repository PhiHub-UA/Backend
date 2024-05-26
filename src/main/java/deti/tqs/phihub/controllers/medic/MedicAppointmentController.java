package deti.tqs.phihub.controllers.medic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/medic/appointments")
public class MedicAppointmentController {

    private MedicService medicService;
    private AppointmentService appointmentService;

    String unauthReason = "Unauthorized";

    @Autowired
    public MedicAppointmentController(MedicService medicService, AppointmentService appointmentService) {
        this.medicService = medicService;
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "Add notes to an appointment", description = "Add notes to an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes added"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PostMapping("/{appointmentId}/notes")
    public ResponseEntity<String> addNotesToAppointment(@RequestBody String notes,
            @PathVariable("appointmentId") Long appointmentId) {

        var medic = medicService.getMedicFromContext();

        if (medic == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, unauthReason);
        }

        var appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found");

        }

        if (!appointment.getMedic().getUsername().equals(medic.getUsername())) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, unauthReason);
        }

        appointment.setNotes(notes);

        appointmentService.save(appointment);

        return ResponseEntity.ok(notes);

    }

    @Operation(summary = "Get notes from an appointment", description = "Get notes from an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{appointmentId}/notes")
    public ResponseEntity<String> getNotesFromAppointment(@PathVariable("appointmentId") Long appointmentId) {

        var medic = medicService.getMedicFromContext();

        if (medic == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, unauthReason);
        }

        var appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found");
        }

        if (!appointment.getMedic().getUsername().equals(medic.getUsername())) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, unauthReason);

        }

        return ResponseEntity.ok(appointment.getNotes());

    }

}
