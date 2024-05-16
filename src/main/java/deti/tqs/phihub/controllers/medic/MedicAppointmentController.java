package deti.tqs.phihub.controllers.medic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/medic/appointments")
public class MedicAppointmentController {

    private MedicService medicService;
    private AppointmentService appointmentService;

    @Autowired
    public MedicAppointmentController(MedicService medicService, AppointmentService appointmentService) {
        this.medicService = medicService;
        this.appointmentService = appointmentService;
    }


    @PostMapping("/{appointmentId}/notes")
    public ResponseEntity<String> addNotesToAppointment(@RequestBody String notes, @PathVariable("appointmentId") Long appointmentId) {

        var medic = medicService.getMedicFromContext();

        if (medic == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        var appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (!appointment.getMedic().getUsername().equals(medic.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        appointment.setNotes(notes);

        appointmentService.save(appointment);

        return ResponseEntity.ok(notes);

    }

    @GetMapping("/{appointmentId}/notes")
    public ResponseEntity<String> getNotesFromAppointment(@PathVariable("appointmentId") Long appointmentId) {

        var medic = medicService.getMedicFromContext();

        if (medic == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        var appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (!appointment.getMedic().getUsername().equals(medic.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(appointment.getNotes());

    }

}
