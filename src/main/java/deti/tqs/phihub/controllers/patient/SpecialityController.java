package deti.tqs.phihub.controllers.patient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import deti.tqs.phihub.services.SpecialityService;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/patient/speciality")
public class SpecialityController {

    private static final Logger logger = LoggerFactory.getLogger(SpecialityController.class);

    private SpecialityService specialityService;

    public SpecialityController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @Operation(summary = "Get specialities", description = "Get specialities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Specialities retrieved"),
    })
    @GetMapping
    public List<String> getSpecialities() {
        logger.info("Patient requested specialities");
        return specialityService.getSpecialities();
    }
    
}
