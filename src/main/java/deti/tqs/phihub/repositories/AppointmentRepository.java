package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    
}
