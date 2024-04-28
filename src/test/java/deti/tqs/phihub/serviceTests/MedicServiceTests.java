package deti.tqs.phihub.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.repositories.MedicRepository;
import deti.tqs.phihub.services.MedicService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MedicServiceTests {

    @Mock(lenient = true)
    private MedicRepository medicRepository;

    @InjectMocks
    private MedicService medicService;

    private Medic medic0 = new Medic();
    private Medic medic1 = new Medic();

    @BeforeEach
    public void setUp() {
        //  Create two medics
        medic0.setId(1L);
        medic0.setName("Josefino");
        medic0.setSpecialities(Arrays.asList(Speciality.CARDIOLOGY, Speciality.HEMATOLOGY));

        medic1.setId(2L);
        medic1.setName("Joana");
        medic1.setSpecialities(Arrays.asList(Speciality.PSYCHIATRY, Speciality.HEMATOLOGY));

        List<Medic> allMedics = Arrays.asList(medic0, medic1);

        Mockito.when(medicRepository.save(medic0)).thenReturn(medic0);
        Mockito.when(medicRepository.save(medic1)).thenReturn(medic1);
        Mockito.when(medicRepository.findAll()).thenReturn(allMedics);
        Mockito.when(medicRepository.findById(medic0.getId())).thenReturn(Optional.of(medic0));
        Mockito.when(medicRepository.findById(medic1.getId())).thenReturn(Optional.of(medic1));
        Mockito.when(medicRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
     void whenSaveValidMedic_thenMedicShouldBeReturned() {
        Medic returned = medicService.save(medic0);
        assertThat(returned.getName()).isEqualTo(medic0.getName());

        returned = medicService.save(medic1);
        assertThat(returned.getName()).isEqualTo(medic1.getName());
    }

    @Test
     void whenSearchValidID_thenMedicshouldBeFound() {
        Medic found = medicService.getMedicById(medic0.getId());
        assertThat(found.getName()).isEqualTo(medic0.getName());

        found = medicService.getMedicById(medic1.getId());
        assertThat(found.getName()).isEqualTo(medic1.getName());
    }

    @Test
     void whenSearchInvalidID_thenMedicShouldNotBeFound() {
        Medic fromDb = medicService.getMedicById(-99L);
        assertThat(fromDb).isNull();

        Mockito.verify(medicRepository, 
                VerificationModeFactory.times(1))
                    .findById(-99L);
    }

    @Test
     void given2Medics_whengetAll_thenReturn2Records() {

        List<Medic> allMedics = medicService.findAll();

        assertThat(allMedics).hasSize(2).extracting(Medic::getSpecialities).contains(medic0.getSpecialities(), medic1.getSpecialities());

        Mockito.verify(medicRepository, 
                VerificationModeFactory.times(1))
                    .findAll();
    }
}
