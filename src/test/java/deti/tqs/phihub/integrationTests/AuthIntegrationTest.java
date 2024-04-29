package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.User;

import io.restassured.RestAssured;
import io.restassured.http.Header;

import static org.hamcrest.Matchers.*;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Date;
import java.util.HashMap;


import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class AuthIntegrationTests {
    
    private final static String BASE_URI = "http://localhost";
 
    @LocalServerPort
    private int port;

    private String loginToken;
    private User user0 = new User();

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;

        //  Create a user
        user0.setId(1L);
        user0.setName("Josefino");
        user0.setUsername("josefino");
        user0.setEmail("jose@fino.com");
        user0.setPhone("919828737");
        user0.setRole("admin");
        user0.setEmail("jose@gino.com");
        user0.setAge(27);
        user0.setPassword("strongPassword");
    }

    @Test
    @DisplayName("When post a Appointment return a Appointment")
    void whenPostValidAppointment_thenCreateAppointment() throws Exception {

        given().port(port)
            .contentType("application/json")
            .body("{\"name\":\"" + user0.getName() + "\"," +
                    "\"phone\":\"" + user0.getPhone() + "\"," +
                    "\"email\":\"" + user0.getEmail() + "\"," +
                    "\"age\":\"" + user0.getAge() + "\"," +
                    "\"username\":\"" + user0.getUsername() + "\"," +
                    "\"password\":\"" + user0.getPassword() + "\"," +
                    "\"role\":\"" + user0.getRole() + "\"}")
            .when()
            .post("/auth/register")
            .then()
            .statusCode(201);

        HashMap<String, String> response = given().port(port)
            .contentType("application/json")
            .body("{\"username\":\"" + user0.getUsername() + "\"," +
                   "\"password\":\"" + user0.getPassword() + "\"}")
            .when()
            .post("/auth/login")
            .then()
            .statusCode(200)
            .extract()
            .as(HashMap.class);

        loginToken = response.get("token");
    }
}