package deti.tqs.phihub.controllers.medic;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Medic;

@RestController
@RequestMapping("/medic")
public class MedicController {

    private AppointmentService appointmentService;
    private MedicService medicService;

    @Autowired
    public MedicController(AppointmentService appointmentService, MedicService medicService) {
        this.appointmentService = appointmentService;
        this.medicService = medicService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getMedicAppointments() {

        Medic medic = medicService.getMedicFromContext();

        if (medic == null) {
            return ResponseEntity.status(401).build();

        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByMedic(medic));
    
    }


    
}
