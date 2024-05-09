package deti.tqs.phihub.controllers;

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
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.services.UserService;
import deti.tqs.phihub.dtos.AppointmentSchema;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    private UserService userService;

    private MedicService medicService;


    public AppointmentController(AppointmentService appointmentService, UserService userService, MedicService medicService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.medicService = medicService;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentSchema appointmentSchema) {

        var user = userService.getUserFromContext();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        var medic = medicService.getMedicById(appointmentSchema.medicID());
        if (medic == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Appointment app = new Appointment();
        app.setPatient(user);
        app.setSpeciality(appointmentSchema.speciality());
        app.setDate(appointmentSchema.date());
        app.setPrice(appointmentSchema.price());
        app.setMedic(medic);
        app.setBill(null);
        
        Appointment savedApp = appointmentService.save(app);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApp);
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
