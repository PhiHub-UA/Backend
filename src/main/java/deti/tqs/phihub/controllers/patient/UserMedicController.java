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

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/patient/medics")
public class UserMedicController {

    private MedicService medicService;

    public UserMedicController(MedicService medicService) {
        this.medicService = medicService;
    }

    @Operation(summary = "Get medics", description = "Get medics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medics retrieved"),
    })
    @GetMapping
    public List<Medic> getMedics(@RequestParam(value = "speciality", required = false) String speciality) {

        if (Speciality.fromString(speciality) != null) {
            return medicService.getMedicsBySpeciality(Speciality.fromString(speciality));
        }
        return medicService.getMedics();
    }

    @Operation(summary = "Get medic", description = "Get medic by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medic retrieved"),
            @ApiResponse(responseCode = "404", description = "Medic not found")
    })
    @GetMapping("/availability/{id}")
    public List<String> getMedicAvailability(@PathVariable(value = "id") Long id,
            @RequestParam(value = "day") Long dateTimestamp) {

        return medicService.getMedicAvailability(id, dateTimestamp);
    }



}

