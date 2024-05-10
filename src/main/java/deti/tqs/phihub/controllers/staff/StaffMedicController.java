package deti.tqs.phihub.controllers.staff;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/staff/medics")
public class StaffMedicController {

    private MedicService medicService;

    @Autowired
    public StaffMedicController(MedicService medicService) {
        this.medicService = medicService;
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
