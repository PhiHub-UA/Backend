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

@RestController
@RequestMapping("/staff")
public class StaffController {

    private StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/me")
    public ResponseEntity<Staff> getLoggedInStaff() {

        var staff = staffService.getStaffFromContext();

        if (staff == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(staff);
    }

    @PostMapping
    public ResponseEntity<Staff> createStaff(@RequestBody StaffSchema staffSchema) {
        var requestUser = staffService.getStaffFromContext();

        Collection<? extends GrantedAuthority> authorities = requestUser.getAuthorities();

        if (!authorities.contains(new SimpleGrantedAuthority("staff"))) {
            return ResponseEntity.status(401).build();
        }

        var staff = staffService.createStaff(staffSchema);

        if (staff == null) {
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.ok(staff);

    }

    @GetMapping("/permissions")
    public ResponseEntity<List<String>> getPermissions() {
        return ResponseEntity.ok(StaffPermissions.getPermissions());
    }

    @GetMapping
    public ResponseEntity<Iterable<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.findAll());
    }

}
