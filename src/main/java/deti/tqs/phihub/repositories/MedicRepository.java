package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.Medic;

@Repository
public interface MedicRepository extends JpaRepository<Medic, Long>{
    
}

