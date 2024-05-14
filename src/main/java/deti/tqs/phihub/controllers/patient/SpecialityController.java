package deti.tqs.phihub.controllers.patient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import deti.tqs.phihub.services.SpecialityService;

@RestController
@RequestMapping("/patient/speciality")
public class SpecialityController {


    private SpecialityService specialityService;

    public SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping
    public List<String> getSpecialities() {
        return specialityService.getSpecialities();
    }
    
}
