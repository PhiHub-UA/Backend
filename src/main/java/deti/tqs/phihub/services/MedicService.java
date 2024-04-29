package deti.tqs.phihub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.repositories.AppointmentRepository;
import deti.tqs.phihub.repositories.MedicRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

@Service
public class MedicService {

    private MedicRepository medicRepository;

    private AppointmentRepository appointmentRepository;

    @Autowired
    public MedicService(MedicRepository medicRepository, AppointmentRepository appointmentRepository) {
        this.medicRepository = medicRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Medic save(Medic medic) {
        return medicRepository.save(medic);
    }

    public Medic getMedicById(Long id) {
        return medicRepository.findById(id).orElse(null);
    }

    public List<Medic> findAll() {
        return medicRepository.findAll();
    }

    public List<Medic> getMedicsBySpeciality(Speciality speciality) {

        return medicRepository.findAll().stream()
                .filter(medic -> medic.getSpecialities() != null &&
                        medic.getSpecialities().contains(speciality))
                .toList();
    }

    public List<Medic> getMedics() {
        return medicRepository.findAll();
    }

    public List<String> getMedicAvailability(Long id, Long dateTimestamp) {

        Medic medic = medicRepository.findById(id).orElse(null);

        if (medic == null) {
            return null;
        }

        Date date = new Date(dateTimestamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // Set the time to the start of the day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // Convert back to a timestamp
        long startOfDayTimestamp = calendar.getTimeInMillis();

        List<Appointment> appointments = appointmentRepository.findByMedicId(id);
        System.out.println("Appointments: " + appointments.size());
        List<Long> availableSlots = new ArrayList<>();
        List<Long> unavailableSlots = new ArrayList<>();

        // Iterate through the appointments
        for (Appointment appointment : appointments) {
            if (appointment.getDate() >= startOfDayTimestamp
                    && appointment.getDate() < startOfDayTimestamp + 24 * 60 * 60 * 1000) {
                long appointmentStartTime = appointment.getDate();
                unavailableSlots.add(appointmentStartTime);
                
            }
        }

        System.out.println("Unavailable slots: " + unavailableSlots.size());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startOfConsultations = calendar.getTimeInMillis();

        // for every 1 hour slot from 9 to 17, check if they are in the unavailable
        // slots, if not add them to available

        for (int i = 0; i < 9; i++) {
            long slot = startOfConsultations + i * 60 * 60 * 1000;
            if (!unavailableSlots.contains(slot)) {
                availableSlots.add(slot);
            }
        }

        System.out.println("Available slots: " + availableSlots.size());

        // now convert all this slots to this format : "HH:MM"
        List<String> availableSlotsString = new ArrayList<>();

        for (int i = 0; i < availableSlots.size(); i++) {
            Date slotDate = new Date(availableSlots.get(i));
            calendar2.setTime(slotDate);
            int hour = calendar2.get(Calendar.HOUR_OF_DAY);
            int minute = calendar2.get(Calendar.MINUTE);
            String slotString = String.format("%02d:%02d", hour, minute);
            availableSlotsString.add(slotString);
        }

        return availableSlotsString;
    }

}
