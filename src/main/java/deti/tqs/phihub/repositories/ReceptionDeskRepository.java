package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.ReceptionDesk;
import java.util.List;

@Repository
public interface ReceptionDeskRepository extends JpaRepository<ReceptionDesk, Long>{

    @Query("SELECT r FROM ReceptionDesk r WHERE r.working = true ORDER BY r.deskNumber ASC")
    public List<ReceptionDesk> findAllAvailable();

    public ReceptionDesk findByDeskNumber(int deskNumber);


    
}