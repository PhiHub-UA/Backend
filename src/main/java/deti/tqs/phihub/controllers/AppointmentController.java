package deti.tqs.phihub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import deti.tqs.phihub.models.Appointment;

import deti.tqs.phihub.services.AppointmentService;

import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.UserService;
import deti.tqs.phihub.DTOs.AppointmentSchema;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    private UserService userService;

    private SpecialityService specialityService;

    // private BillService billService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserService userService,
            SpecialityService specialityService
    // BillService billService
    ) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.specialityService = specialityService;
        // this.billService = billService;

    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentSchema appointmentSchema) {
        var user = userService.getUserFromContext();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var speciality = specialityService.getSpecialityById(appointmentSchema.specialityId());
        if (speciality == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var appointment = appointmentService.createAppointment(user, speciality, appointmentSchema.date(),
                appointmentSchema.price());
        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointments() {
        var user = userService.getUserFromContext();
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(user);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        var user = userService.getUserFromContext();
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment.getPatient().getId() != user.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(appointment);
    }

}
