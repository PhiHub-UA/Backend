package deti.tqs.phihub.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mock.Strictness;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.phihub.dtos.StaffSchema;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.repositories.StaffRepository;
import deti.tqs.phihub.services.StaffService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class StaffServiceTests {

    @Mock(strictness = Strictness.LENIENT)
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffService staffService;

    private Staff staff0 = new Staff();
    StaffSchema staff0Schema;

    @BeforeEach
    public void setUp() {
        //  Create two staffs
        staff0.setId(1L);
        staff0.setUsername("Josefino");
        staff0.setAge(39);

        staff0Schema = new StaffSchema("0", "josefino@staff.com", staff0.getAge(), staff0.getUsername(), "josestaff", "jos123", List.of());

        Mockito.when(staffRepository.save(Mockito.any())).thenReturn(staff0);
        Mockito.when(staffRepository.findByUsername(staff0.getUsername())).thenReturn(staff0);
    }

    @Test
     void whenSaveValidStaff_thenStaffShouldBeReturned() {
        Staff returned = staffService.createStaff(staff0Schema);
        assertThat(returned.getUsername()).isEqualTo(staff0.getUsername());
    }

    @Test
     void whenStaffContext_thenStaffShouldNotBeFound() {
        assertThatThrownBy(() -> staffService.getStaffFromContext()).isInstanceOf(NullPointerException.class);
    }
}
