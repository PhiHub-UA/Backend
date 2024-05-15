package deti.tqs.phihub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.repositories.StaffRepository;
import deti.tqs.phihub.dtos.StaffSchema;
import deti.tqs.phihub.models.Staff;

@Service
public class StaffService {

    private StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff getStaffFromContext() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return (Staff)SecurityContextHolder.getContext().getAuthentication().getPrincipal();  
    }

    public Staff createStaff(StaffSchema staff) {
        Staff staffMember = new Staff(staff.name(), staff.phone(), staff.email(), staff.age(), staff.username(), staff.password());
        return staffRepository.save(staffMember);
    }
}
