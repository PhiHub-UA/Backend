package deti.tqs.phihub.controllers.staff;

import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.services.AppointmentService;

import deti.tqs.phihub.services.StaffService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.http.ResponseEntity;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/staff/appointments")
public class StaffAppointmentController {

    private StaffService staffService;
    private AppointmentService appointmentService;

    public StaffAppointmentController(StaffService staffService, AppointmentService appointmentService) {
        this.staffService = staffService;
        this.appointmentService = appointmentService;
    }

    @PutMapping("/{appointmentId}/issue_bill")
    @Operation(summary = "Issue bill for an appointment", description = "Issue bill for an appointment")
    public ResponseEntity<String> issueBillForAppointment(Long appointmentId) {

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

        return ResponseEntity.ok("Bill issued");
    }





    
}
