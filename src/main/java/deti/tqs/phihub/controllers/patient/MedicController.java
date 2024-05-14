package deti.tqs.phihub.controllers.patient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.services.MedicService;
import java.util.List;


@RestController
@RequestMapping("/patient/medics")
public class MedicController {

    private MedicService medicService;

    public MedicController(MedicService medicService) {
        this.medicService = medicService;
    }

    @GetMapping
    public List<Medic> getMedics(@RequestParam(value = "speciality", required = false) String speciality) {

        if (Speciality.fromString(speciality) != null) {
            return medicService.getMedicsBySpeciality(Speciality.fromString(speciality));
        }
        return medicService.getMedics();
    }

    @GetMapping("/availability/{id}")
    public List<String> getMedicAvailability(@PathVariable(value = "id") Long id,
            @RequestParam(value = "day") Long dateTimestamp) {

        return medicService.getMedicAvailability(id, dateTimestamp);
    }

}

