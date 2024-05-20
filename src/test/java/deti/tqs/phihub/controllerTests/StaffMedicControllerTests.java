package deti.tqs.phihub.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.phihub.configs.SecurityFilter;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.controllers.staff.StaffMedicController;
import deti.tqs.phihub.dtos.MedicSchema;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.StaffPermissions;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.StaffService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(StaffMedicController.class)
@AutoConfigureMockMvc(addFilters = false)
class StaffMedicControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MedicService service;

    @MockBean
    private StaffService staffService;
    @MockBean
    private SpecialityService specialityService;
    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private Staff staff0 = new Staff();
    private Medic medic0 = new Medic();
    MedicSchema medicSchema;

    @BeforeEach
    public void setUp() {
        //  Create a staff
        staff0.setId(1L);
        staff0.setUsername("josefino");
        staff0.setEmail("jose@fino.com");
        staff0.setPhone("919828737");
        staff0.setPermissions(List.of(StaffPermissions.CREATE));
        

        //  Create a medic
        medic0.setId(1L);
        medic0.setEmail("joaquinas@medic.com");
        medic0.setPhone("919727838");
        medic0.setAge(51);
        medic0.setUsername("joaquina");
        medic0.setPassword("joa_quinas123");
        medic0.setName("Joaquinas");
        medic0.setSpecialities(List.of(Speciality.DERMATOLOGY, Speciality.HEMATOLOGY));

        medicSchema = new MedicSchema(medic0.getPhone(), medic0.getEmail(), medic0.getAge(), medic0.getUsername(), medic0.getPassword(), "medic", medic0.getName(), List.of(Speciality.HEMATOLOGY.toString(), Speciality.DERMATOLOGY.toString()));
       
        when(service.save(Mockito.any())).thenReturn(medic0);
        when(service.findAll()).thenReturn(List.of(medic0));
        when(service.getMedicFromContext()).thenReturn(medic0);
    }

    @Test
    void givenOneMedicLoggedIn_thenReturnIt() throws Exception {

        mvc.perform(
                get("/staff/medics/me").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone", is(medic0.getPhone())));

        verify(service, times(1)).getMedicFromContext();
    }

    @Test
    void givenOneMedicLoggedIn_WhenReturnAll_thenReturnItInAList() throws Exception {

        mvc.perform(
                get("/staff/medics").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phone", is(medic0.getPhone())));

        verify(service, times(1)).findAll();
    }

    @Test
    void givenOneMedic_thenCreateIt() throws Exception {

        mvc.perform(
                post("/staff/medics").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"phone\":\"" + medicSchema.phone() +
                        "\",\"email\":\"" + medicSchema.email() +
                        "\",\"age\":" + medicSchema.age() +
                        "  ,\"username\":\"" + medicSchema.username() +
                        "\",\"name\":\"" + medicSchema.name() +
                        "\",\"role\":\"medic"  +
                        "\",\"specialities\":[\"" + Speciality.HEMATOLOGY.toString() +
                        "\"],\"password\":\"" + medicSchema.password() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phone", is(medic0.getPhone())));

        verify(service, times(1)).save(Mockito.any());
    }

    @Test
    void givenMedicNotLoggedIn_whenGet_thenReturnError() throws Exception {
        //  Check medic bad logged in
        when(service.getMedicFromContext()).thenReturn(null);
        
        mvc.perform(
            get("/staff/medics/me").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
}