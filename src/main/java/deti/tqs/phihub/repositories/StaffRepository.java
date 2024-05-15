package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>{

    public Staff findByUsername(String username);

    
}
