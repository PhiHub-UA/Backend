package deti.tqs.phihub.services;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.repositories.StaffRepository;
import deti.tqs.phihub.dtos.StaffSchema;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.models.StaffPermissions;
import org.springframework.security.core.Authentication;
import java.util.List;

@Service
public class StaffService {

    private StaffRepository staffRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff getStaffFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
    
        Object principal = authentication.getPrincipal();
        if (principal instanceof Staff staff) {
            return (Staff) principal;
        }
    
        return null;
    }

    public Staff createStaff(StaffSchema staff) {
        Staff staffMember = new Staff(staff.name(), staff.phone(), staff.email(), staff.age(), staff.username(),
                passwordEncoder.encode(staff.password()), StaffPermissions.fromStrings(staff.permissions()));
        return staffRepository.save(staffMember);
    }

    public List<Staff> findAll() {
        return staffRepository.findAll();
    }
}
