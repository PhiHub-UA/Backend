package deti.tqs.phihub.services;


import java.util.List;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.AppointmentRepository;
import deti.tqs.phihub.repositories.BillRepository;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.AppointmentState;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Bill;
import java.util.Date;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private BillRepository billRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, BillRepository billRepository) {
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
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

    public boolean issueBill(Appointment app) {

        if (app == null) {
            return false;
        }

        Bill bill = new Bill();
        bill.setDate(new Date());
        bill.setPrice(app.getPrice());
        bill.setAppointmentID(app.getId());

        app.setBill(bill);
        app.setState(AppointmentState.BILL_ISSUED);

        appointmentRepository.save(app);
        billRepository.save(bill);

        return true;
    }

    public boolean payAppointment(Appointment app) {

        if (app == null) {
            return false;
        }

        app.setState(AppointmentState.BILL_PAID);
        app.getBill().setPaid(true);

        appointmentRepository.save(app);
        billRepository.save(app.getBill());

        return true;
    }
}
