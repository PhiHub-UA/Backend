package deti.tqs.phihub.repositoryTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.repositories.MedicRepository;

import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class MedicRepositoryTests {

    @Autowired
    private MedicRepository medicRepository;

    private Medic medic0 = new Medic();
    private Medic medic1 = new Medic();

    @BeforeAll
    public void setUp() throws Exception {

        medic0.setId(1L);
        medic0.setName("Josefino");
        medic0.setSpecialities(Arrays.asList(Speciality.CARDIOLOGY, Speciality.HEMATOLOGY));
        
        medicRepository.saveAndFlush(medic0);

        medic1.setId(2L);
        medic1.setName("Joana");
        medic1.setSpecialities(Arrays.asList(Speciality.PSYCHIATRY, Speciality.HEMATOLOGY));
    }

    @Test
    void whenFindMedicById_thenReturnMedic() {

        Medic found = medicRepository.findById(medic0.getId()).get();
        
        assertThat(found.getName()).isEqualTo(medic0.getName());
    }

    @Test
    void whenInvalidMedicId_thenReturnNull() {
        Medic found = medicRepository.findById(123L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void whenMedicIsDeleted_thenReturnNull() {
        medicRepository.delete(medic0);

        Medic found = medicRepository.findById(medic0.getId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfMedics_whenFindAll_thenReturnAllMedics() {
        //  Save the medic 1
        medicRepository.saveAndFlush(medic1);

        List<Medic> allMedics = medicRepository.findAll();

        assertThat(allMedics).hasSize(2).extracting(Medic::getSpecialities).containsOnly(medic0.getSpecialities(), medic1.getSpecialities());
    }
}