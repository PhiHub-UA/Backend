package deti.tqs.phihub.controllers.staff;

import org.springframework.web.bind.annotation.RestController;

import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.services.StaffService;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import deti.tqs.phihub.dtos.StaffSchema;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import deti.tqs.phihub.models.StaffPermissions;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/staff")
public class StaffController {

    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);
    

    private StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @Operation(summary = "Get logged in staff", description = "Get logged in staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<Staff> getLoggedInStaff() {

        var staff = staffService.getStaffFromContext();

        if (staff == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        logger.info("Staff {} requested his/her information", staff.getUsername());

        return ResponseEntity.ok(staff);
    }

    @Operation(summary = "Create staff", description = "Create staff user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<Staff> createStaff(@RequestBody StaffSchema staffSchema) {
        var requestUser = staffService.getStaffFromContext();

        Collection<? extends GrantedAuthority> authorities = requestUser.getAuthorities();

        if (!authorities.contains(new SimpleGrantedAuthority("staff"))) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        var staff = staffService.createStaff(staffSchema);

        if (staff == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Bad request");
        }

        logger.info("Staff {} created a new staff user", requestUser.getUsername());

        return ResponseEntity.ok(staff);

    }

    @Operation(summary = "Get staff permissions", description = "Get staff permissions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions retrieved")
    })
    @GetMapping("/permissions")
    public ResponseEntity<List<String>> getPermissions() {
        logger.info("Staff requested permissions");
        return ResponseEntity.ok(StaffPermissions.getPermissions());
    }

    @Operation(summary = "Get all staff", description = "Get all staff")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff retrieved")
    })
    @GetMapping
    public ResponseEntity<Iterable<Staff>> getAllStaff() {
        logger.info("Staff requested all staff");
        return ResponseEntity.ok(staffService.findAll());
    }

}
