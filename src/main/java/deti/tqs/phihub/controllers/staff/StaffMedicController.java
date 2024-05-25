package deti.tqs.phihub.controllers.staff;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.services.MedicService;
import org.springframework.web.bind.annotation.RequestBody;
import deti.tqs.phihub.dtos.MedicSchema;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/staff/medics")
public class StaffMedicController {

    private MedicService medicService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public StaffMedicController(MedicService medicService) {
        this.medicService = medicService;
    }   


    @Operation(summary = "Create medic", description = "Create medic user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medic created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<Medic> saveMedic(@RequestBody MedicSchema medicSchema) {

        Medic medic = new Medic();
        medic.setName(medicSchema.name());
        medic.setAge(medicSchema.age());
        medic.setEmail(medicSchema.email());
        medic.setPhone(medicSchema.phone());
        medic.setUsername(medicSchema.username());
        medic.setPassword(passwordEncoder.encode(medicSchema.password()));
        medic.setSpecialities(Speciality.fromStrings(medicSchema.specialities()));

        Medic savedMedic = medicService.save(medic);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedic);
    }

    @Operation(summary = "Get all medics", description = "Get all medics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medics retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<Iterable<Medic>> getAllMedics() {
        return ResponseEntity.ok(medicService.findAll());
    }
    
    @Operation(summary = "Get logged in medic", description = "Get logged in medic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medic retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<Medic> getLoggedInMedic() {

        var medic = medicService.getMedicFromContext();

        if (medic == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(medic);

    }

}
