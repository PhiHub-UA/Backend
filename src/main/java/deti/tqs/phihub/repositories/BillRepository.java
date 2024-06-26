package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>{
    
}
