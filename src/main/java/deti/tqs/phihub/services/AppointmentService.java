package deti.tqs.phihub.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.AppointmentRepository;
import deti.tqs.phihub.models.Appointment;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(User user, Speciality speciality, Date date, Double price) {
        var appointment = new Appointment();
        appointment.setPatient(user);
        appointment.setSpeciality(speciality);
        appointment.setDate(date);
        appointment.setPrice(price);
        appointment.setBill(null);
        return appointmentRepository.save(appointment);

    }

}
