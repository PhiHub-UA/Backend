package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
    
}
