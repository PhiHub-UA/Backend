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
import deti.tqs.phihub.controllers.staff.StaffAppointmentController;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.AppointmentState;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.models.Bill;
import deti.tqs.phihub.models.StaffPermissions;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.services.StaffService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(StaffAppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StaffAppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StaffService service;

    @MockBean
    private MedicService medicService;
    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private Staff staff0 = new Staff();
    private Medic medic0 = new Medic();
    private User user0 = new User();
    private Bill bill0 = new Bill();
    private Appointment app0 = new Appointment();

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
        
        //  Create a appointment
        app0.setId(1L);
        app0.setPrice(16.02);
        app0.setMedic(medic0);
        app0.setPatient(user0);
        app0.setNotes("Standard appointment");
        app0.setState(AppointmentState.CHECKED_IN);

        //  Create Issue bill
        bill0.setId(1L);
        bill0.setAppointmentID(1L);
        bill0.setPaid(false);
        
        when(service.getStaffFromContext()).thenReturn(staff0);
        when(appointmentService.getAppointmentById(Mockito.any())).thenReturn(app0);
        when(appointmentService.findAll()).thenReturn(List.of(app0));
        when(appointmentService.issueBill(app0)).thenReturn(true);
    }

    @Test
    void givenAppointment_issueTheBill() throws Exception {

        mvc.perform(
                put("/staff/appointments/1/issue_bill").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Bill issued")));

        verify(appointmentService, times(1)).getAppointmentById(Mockito.any());
    }

    @Test
    void givenOneAppointment_ReturnIt() throws Exception {

        mvc.perform(
                get("/staff/appointments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price", is(app0.getPrice())));

        verify(appointmentService, times(1)).findAll();
    }

    @Test
    void givenBadStaffPut_returnError() throws Exception {

        when(service.getStaffFromContext()).thenReturn(null);

        mvc.perform(
                put("/staff/appointments/1/issue_bill").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(appointmentService, times(0)).getAppointmentById(Mockito.any());
    }

    @Test
    void givenBadAppointmentIDPut_returnError() throws Exception {

        when(appointmentService.getAppointmentById(Mockito.any())).thenReturn(null);

        mvc.perform(
                put("/staff/appointments/1/issue_bill").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(0)).issueBill(Mockito.any());
    }

    @Test
    void givenBadIssueBill_returnError() throws Exception {

        when(appointmentService.issueBill(app0)).thenReturn(false);

        mvc.perform(
                put("/staff/appointments/1/issue_bill").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenBadStaffGet_returnError() throws Exception {

        when(service.getStaffFromContext()).thenReturn(null);

        mvc.perform(
                get("/staff/appointments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(service, times(0)).findAll();
    }
}