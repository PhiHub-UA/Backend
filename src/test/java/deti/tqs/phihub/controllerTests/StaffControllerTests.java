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
import deti.tqs.phihub.controllers.staff.StaffController;
import deti.tqs.phihub.dtos.StaffSchema;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.StaffService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(StaffController.class)
@AutoConfigureMockMvc(addFilters = false)
class StaffControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StaffService service;

    @MockBean
    private SpecialityService specialityService;
    @MockBean
    private AppointmentService appointmentService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private Staff staff0 = new Staff();
    StaffSchema staff0Schema;

    @BeforeEach
    public void setUp() {
        //  Create a staff
        staff0.setId(1L);
        staff0.setUsername("josefino");
        staff0.setEmail("jose@fino.com");
        staff0.setPhone("919828737");
        staff0Schema = new StaffSchema("0", "josefino@staff.com", staff0.getAge(), staff0.getUsername(), "josestaff", "jos123", null);
        
        when(service.getStaffFromContext()).thenReturn(staff0);
        when(service.createStaff(Mockito.any())).thenReturn(staff0);
    }

    @Test
    void givenOneStaffsLoggedIn_thenReturnIt() throws Exception {

        mvc.perform(
                get("/staff/me").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone", is(staff0.getPhone())));

        verify(service, times(1)).getStaffFromContext();
    }

    @Test
    void givenOneStaffs_thenCreateIt() throws Exception {

        mvc.perform(
                post("/staff").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"phone\":\"" + staff0Schema.phone() +
                        "\",\"email\":\"" + staff0Schema.email() +
                        "\",\"age\":" + staff0Schema.age() +
                        "  ,\"username\":\"" + staff0Schema.username() +
                        "\",\"name\":\"" + staff0Schema.name() +
                        "\",\"password\":\"" + staff0Schema.password() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone", is(staff0.getPhone())));

        verify(service, times(1)).getStaffFromContext();
    }

    @Test
    void givenStaffNotLoggedIn_whenGet_thenReturnError() throws Exception {
        //  Check staff bad logged in
        when(service.getStaffFromContext()).thenReturn(null);
        
        mvc.perform(
            get("/staff/me").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
}