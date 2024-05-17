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
import deti.tqs.phihub.controllers.medic.MedicController;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.UserService;
import net.minidev.json.JSONArray;
import deti.tqs.phihub.services.MedicService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(MedicController.class)
@AutoConfigureMockMvc(addFilters = false)
class PatientMedicControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MedicService service;

    @MockBean
    private UserService userService;
    @MockBean
    private SpecialityService specialityService;
    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private Medic medic0 = new Medic();
    private User user0 = new User();

    @BeforeEach
    public void setUp() {
        //  Create a user
        user0.setId(1L);
        user0.setUsername("josefino");
        user0.setEmail("jose@fino.com");
        user0.setPhone("919828737");
        
        when(userService.getUserFromContext()).thenReturn(user0);

        //  Create a medic
        medic0.setId(1L);
        medic0.setName("josefino");
        medic0.setSpecialities(List.of(Speciality.CARDIOLOGY, Speciality.HEMATOLOGY));
        
        when(service.getMedicFromContext()).thenReturn(medic0);
    }

    @Test
    void givenOneMedics_thenReturnIt() throws Exception {
        mvc.perform(
                get("/medic/appointments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(new JSONArray())));

        verify(appointmentService, times(1)).getAppointmentsByMedic(Mockito.any());
    }

}