package deti.tqs.phihub.controllers.medic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.User;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/medic/patients")
public class MedicPatientsController {

    private MedicService medicService;
    private AppointmentService appointmentService;

    public MedicPatientsController(MedicService medicService, AppointmentService appointmentService) {
        this.medicService = medicService;
        this.appointmentService = appointmentService;

    }

    @GetMapping
    public ResponseEntity<Set<User>> getMedicPatients() {

        var medic = medicService.getMedicFromContext();

        if (medic == null) {
            return ResponseEntity.status(401).build();
        }

        List<Appointment> appointments = appointmentService.getAppointmentsByMedic(medic);

        Set<User> patients = new HashSet<>();

        for (Appointment appointment : appointments) {
            patients.add(appointment.getPatient());
        }

        return ResponseEntity.ok(patients);

    }

}
