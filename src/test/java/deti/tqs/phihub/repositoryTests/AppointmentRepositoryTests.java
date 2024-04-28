package deti.tqs.phihub.repositoryTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.repositories.AppointmentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class AppointmentRepositoryTests {

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Appointment app0 = new Appointment();
    private Appointment app1 = new Appointment();

    @BeforeAll
    public void setUp() throws Exception {

        app0.setId(1L);
        app0.setPrice(12.3);
        app0.setSpeciality(Speciality.NEUROLOGY);
        
        appointmentRepository.saveAndFlush(app0);

        app1.setId(2L);
        app1.setPrice(25.7);
    }

    @Test
    void whenFindAppointmentById_thenReturnAppointment() {

        Appointment found = appointmentRepository.findById(app0.getId()).get();
        
        assertThat(found.getPrice()).isEqualTo(app0.getPrice());
        assertThat(found.getSpeciality()).isEqualTo(app0.getSpeciality());
    }

    @Test
    void whenInvalidAppointmentId_thenReturnNull() {
        Appointment found = appointmentRepository.findById(123L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void whenAppointmentIsDeleted_thenReturnNull() {
        appointmentRepository.delete(app0);

        Appointment found = appointmentRepository.findById(app0.getId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfAppointments_whenFindAll_thenReturnAllAppointments() {
        //  Save the appointment 1
        appointmentRepository.saveAndFlush(app1);

        List<Appointment> allAppointments = appointmentRepository.findAll();

        assertThat(allAppointments).hasSize(2).extracting(Appointment::getPrice).containsOnly(app0.getPrice(), app1.getPrice());
    }
}