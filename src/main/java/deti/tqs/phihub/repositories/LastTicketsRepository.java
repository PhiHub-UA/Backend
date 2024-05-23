package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.LastTickets;

@Repository
public interface LastTicketsRepository extends JpaRepository<LastTickets, Long>{
    
}
