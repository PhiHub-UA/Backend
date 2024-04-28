package deti.tqs.phihub.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.AppointmentRepository;
import deti.tqs.phihub.services.AppointmentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTests {

    @Mock(lenient = true)
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment app0 = new Appointment();
    private Appointment app1 = new Appointment();
    private User user0 = new User();

    @BeforeEach
    public void setUp() {

        user0.setId(1L);
        user0.setUsername("Josefino Calças");
        user0.setEmail("jose@fino.com");
        user0.setPhone("929838747");

        //  Create two appointments
        app0.setId(1L);
        app0.setPrice(12.3);
        app0.setSpeciality(Speciality.NEUROLOGY);
        app0.setPatient(user0);
        app1.setId(2L);
        app1.setPrice(25.7);

        List<Appointment> allAppointments = Arrays.asList(app0, app1);

        Mockito.when(appointmentRepository.save(app0)).thenReturn(app0);
        Mockito.when(appointmentRepository.save(app1)).thenReturn(app1);
        Mockito.when(appointmentRepository.findAll()).thenReturn(allAppointments);
        Mockito.when(appointmentRepository.findById(app0.getId())).thenReturn(Optional.of(app0));
        Mockito.when(appointmentRepository.findById(app1.getId())).thenReturn(Optional.of(app1));
        Mockito.when(appointmentRepository.findById(-99L)).thenReturn(Optional.empty());
        Mockito.when(appointmentRepository.findByPatientUsername(user0.getUsername())).thenReturn(Arrays.asList(app0));
    }

    @Test
     void whenSaveValidAppointment_thenAppointmentShouldBeReturned() {
        Appointment returned = appointmentService.save(app0);
        assertThat(returned.getPrice()).isEqualTo(app0.getPrice());

        returned = appointmentService.save(app1);
        assertThat(returned.getPrice()).isEqualTo(app1.getPrice());
    }

    @Test
     void whenSearchValidID_thenAppointmentshouldBeFound() {
        Appointment found = appointmentService.getAppointmentById(app0.getId());
        assertThat(found.getPrice()).isEqualTo(app0.getPrice());

        found = appointmentService.getAppointmentById(app1.getId());
        assertThat(found.getPrice()).isEqualTo(app1.getPrice());
    }

    @Test
     void whenSearchInvalidID_thenAppointmentShouldNotBeFound() {
        Appointment fromDb = appointmentService.getAppointmentById(-99L);
        assertThat(fromDb).isNull();

        Mockito.verify(appointmentRepository, 
                VerificationModeFactory.times(1))
                    .findById(-99L);
    }

    @Test
     void given2Appointments_whengetAll_thenReturn2Records() {

        List<Appointment> allAppointments = appointmentService.findAll();

        assertThat(allAppointments).hasSize(2).extracting(Appointment::getPrice).contains(app0.getPrice(), app1.getPrice());

        Mockito.verify(appointmentRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }

    @Test
     void givenAUserName_whenGetByUser_thenReturn1Record() {

        List<Appointment> userAppointments = appointmentService.getAppointmentsByPatient(user0);

        assertThat(userAppointments).hasSize(1).extracting(Appointment::getPrice).contains(app0.getPrice());

        Mockito.verify(appointmentRepository, 
                VerificationModeFactory.times(1))
                    .findByPatientUsername(user0.getUsername());
    }
}
