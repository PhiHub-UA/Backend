package deti.tqs.phihub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.services.MedicService;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/medic")
public class MedicController {

    private MedicService medicService;

    @Autowired
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

    @PostMapping
    public ResponseEntity<Medic> saveMedic(@RequestParam(value = "name") String name,
            @RequestParam(value = "specialities") List<String> specialities) {

        Medic medic = new Medic();
        medic.setName(name);
        medic.setSpecialities(Speciality.fromStrings(specialities));

        Medic savedMedic = medicService.save(medic);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedic);
    }


}
