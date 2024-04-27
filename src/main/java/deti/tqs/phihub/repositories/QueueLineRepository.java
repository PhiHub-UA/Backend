package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.QueueLine;

@Repository
public interface QueueLineRepository extends JpaRepository<QueueLine, Long>{
    
}