package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Medic;
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
class AppointmentIntegrationTests {
    
    private final static String BASE_URI = "http://localhost";
 
    @LocalServerPort
    private int port;

    private String loginToken;
    private Appointment app0 = new Appointment();
    private User user0 = new User();
    private Medic medic0 = new Medic();

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

        //  Create a appointment
        app0.setId(1L);
        //app0.setDate(new Date());
        app0.setPrice(12.3);
        app0.setPatient(user0);
        app0.setMedic(medic0);

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

    @Test
    @DisplayName("When post a Appointment return a Appointment")
    void whenPostValidAppointment_thenCreateAppointment() throws Exception {

        Appointment app1 = new Appointment();

        app1.setId(2L);
        //app1.setDate(new Date());
        app1.setPrice(24.7);
        app1.setPatient(user0);
        app1.setMedic(medic0);

        HashMap<String, Object> app0Saved = given().port(port)
            .contentType("application/json")
            .header(new Header("Authorization", "Bearer " + loginToken))
            .body("{\"date\": \"1714159929000\"," +
                   "\"price\": " + app0.getPrice().toString() + "," +
                   "\"specialityId\": " + 1 + "," + 
                   "\"medicId\": " + 1 + "}")
            .when()
            .post("/appointments")
            .then()
            .statusCode(201)
            .assertThat()
            .body("date", is("1714159929000"))
            .extract()
            .as(HashMap.class);

        given().port(port)
            .contentType("application/json")
            .header(new Header("Authorization", "Bearer " + loginToken))
            .body("{\"date\": \"1714159929001\", \"price\": \"" + app0.getPrice() + "\", \"specialityId\": \"" + 1 + "\" "+ "\", \"medicId\": \"" + 1 + "\"}")
            .when()
            .when()
            .post("/appointments")
            .then()
            .statusCode(201);

        given().port(port)
            .contentType("application/json")
            .header(new Header("Authorization", "Bearer " + loginToken))
            .when()
            .get("/appointments")
            .then().log().all()
            .statusCode(200)
            .assertThat().
                body("$.size()", equalTo(2)).
                body("[0].price", equalTo(app0.getPrice().floatValue())).
                body("[1].price", equalTo(app1.getPrice().floatValue())).
                body("[0].date", equalTo("1714159929000")).
                body("[1].date", equalTo("1714159929001")).
            extract().as(Appointment[].class);

        given().port(port)
            .contentType("application/json")
            .header(new Header("Authorization", "Bearer " + loginToken))
            .when()
            .get("/appointments/" + app0Saved.get("id"))
            .then().log().all()
            .statusCode(200)
            .assertThat().
                body("price", equalTo(app0.getPrice().floatValue())).
                body("date", equalTo("1714159929000")).
            extract().as(Appointment.class);
    }
}