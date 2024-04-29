package deti.tqs.phihub.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import deti.tqs.phihub.models.Appointment;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

    @Query("SELECT a FROM Appointment a WHERE a.patient.username = ?1")
    public List<Appointment> findByPatientUsername(String username);

    @Query("SELECT a FROM Appointment a WHERE a.medic.id = ?1")
    public List<Appointment> findByMedicId(Long id);
    
}
