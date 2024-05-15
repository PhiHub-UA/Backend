package deti.tqs.phihub.repositoryTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.repositories.StaffRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
class StaffRepositoryTests {

    @Autowired
    private StaffRepository staffRepository;

    private Staff staff0 = new Staff();
    private Staff staff1 = new Staff();

    @BeforeAll
    public void setUp() {

        staff0.setId(1L);
        staff0.setEmail("jose@fino.com");
        staff0.setPhone("757848656");
        
        staffRepository.saveAndFlush(staff0);

        staff1.setId(2L);
        staff1.setEmail("jo@ana.com");
        staff1.setPhone("919010828");
    }

    @Test
    void whenFindStaffById_thenReturnStaff() {

        Staff found = staffRepository.findById(staff0.getId()).get();
        
        assertThat(found.getEmail()).isEqualTo(staff0.getEmail());
        assertThat(found.getPhone()).isEqualTo(staff0.getPhone());
    }

    @Test
    void whenInvalidStaffId_thenReturnNull() {
        Staff found = staffRepository.findById(123L).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void whenStaffIsDeleted_thenReturnNull() {
        staffRepository.delete(staff0);

        Staff found = staffRepository.findById(staff0.getId()).orElse(null);
        assertThat(found).isNull();
    }

    @Test
    void givenSetOfStaffs_whenFindAll_thenReturnAllStaffs() {
        //  Save the staff 1
        staffRepository.saveAndFlush(staff1);

        List<Staff> allStaffs = staffRepository.findAll();

        assertThat(allStaffs).hasSize(2).extracting(Staff::getEmail).containsOnly(staff0.getEmail(), staff1.getEmail());
    }
}