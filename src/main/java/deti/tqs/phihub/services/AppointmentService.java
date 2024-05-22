package deti.tqs.phihub.services;


import java.util.List;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.AppointmentRepository;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.AppointmentState;
import deti.tqs.phihub.models.Medic;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByPatient(User user) {
        return appointmentRepository.findByPatientUsername(user.getUsername());
    }


    public List<Appointment> getAppointmentsByMedic(Medic medic) {
        return appointmentRepository.findByMedicUsername(medic.getUsername());
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }
    
    public void deleteAppointmentById(Long id) {
        appointmentRepository.deleteById(id);
    }

    public boolean updateAppointmentState(Appointment app, AppointmentState state) {

        if (app == null) {
            return false;
        }

        app.setState(state);
        appointmentRepository.save(app);

        return true;
    }
}
