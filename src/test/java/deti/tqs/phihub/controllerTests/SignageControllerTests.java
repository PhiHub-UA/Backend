package deti.tqs.phihub.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.phihub.configs.SecurityFilter;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.controllers.signage.SignageController;
import deti.tqs.phihub.services.LastTicketsService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(SignageController.class)
@AutoConfigureMockMvc(addFilters = false)
class SignageControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LastTicketsService service;

    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    @BeforeEach
    public void setUp() {
        when(service.getParsedTickets()).thenReturn("{\"nextnum\":\"---\"}");
    }

    @Test
    void givenNoNumber_ThenDontCallMore() throws Exception {

        mvc.perform(
                get("/signage").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextnum", is("---")));

        verify(service, times(1)).getParsedTickets();
    }

    @Test
    void givenSeveralNumbers_ThenGetAll() throws Exception {
        when(service.getParsedTickets()).thenReturn("{\"nextnum\":\"---\", \"1\":[\"P16\", 3]}");

        mvc.perform(
                get("/signage").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.1.[0]", is("P16")));

        verify(service, times(1)).getParsedTickets();
    }
}