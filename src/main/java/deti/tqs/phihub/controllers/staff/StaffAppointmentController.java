package deti.tqs.phihub.controllers.staff;

import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.services.AppointmentService;

import deti.tqs.phihub.services.StaffService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.http.ResponseEntity;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/staff/appointments")
public class StaffAppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(StaffAppointmentController.class);

    private StaffService staffService;
    private AppointmentService appointmentService;

    public StaffAppointmentController(StaffService staffService, AppointmentService appointmentService) {
        this.staffService = staffService;
        this.appointmentService = appointmentService;
    }

    @PutMapping("/{appointmentId}/issue_bill")
    @Operation(summary = "Issue bill for an appointment", description = "Issue bill for an appointment")
    public ResponseEntity<String> issueBillForAppointment(@PathVariable ("appointmentId") Long appointmentId) {

        var staff = staffService.getStaffFromContext();

        if (staff == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        var appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found");
        }

        boolean success = appointmentService.issueBill(appointment);

        if (!success) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not issue bill");
        }

        logger.info("Bill issued for appointment {}", appointmentId);

        return ResponseEntity.ok("Bill issued");
    }

    @GetMapping
    @Operation(summary = "Get all appointments", description = "Get all appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {

        var staff = staffService.getStaffFromContext();

        if (staff == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        logger.info("Staff {} requested all appointments", staff.getName());

        return ResponseEntity.ok(appointmentService.findAll());
    }





    
}
