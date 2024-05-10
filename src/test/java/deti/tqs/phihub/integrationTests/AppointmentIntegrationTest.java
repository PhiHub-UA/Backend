package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.dtos.AppointmentSchema;
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
import org.junit.jupiter.api.BeforeAll;
import deti.tqs.phihub.models.Speciality;
import java.util.List;

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

    private static User user0 = new User();
    private static Medic medic0 = new Medic();
    private static String loginToken;


    @BeforeAll
    void setUp() throws Exception {

        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;


        // Create a user
        user0.setId(1L);
        user0.setUsername("zezocas");
        user0.setEmail("josefino123@gmail.com");
        user0.setPhone("987654321");
        user0.setRole("user");
        user0.setAge(27);
        user0.setPassword("strongPassword");

        // Create a medic
        medic0.setId(1L);
        medic0.setName("Dr.Bananas");
        medic0.setSpecialities(List.of(Speciality.CARDIOLOGY, Speciality.DERMATOLOGY, Speciality.ENDOCRINOLOGY));

        given().port(port)
                .contentType("application/json")
                .body("{\"phone\":\"" + user0.getPhone() + "\"," +
                        "\"email\":\"" + user0.getEmail() + "\"," +
                        "\"age\":\"" + user0.getAge() + "\"," +
                        "\"username\":\"" + user0.getUsername() + "\"," +
                        "\"password\":\"" + user0.getPassword() + "\"," +
                        "\"role\":\"" + user0.getRole() + "\"}")
                .when()
                .post("/patient/auth/register")
                .then()
                .statusCode(201);

        HashMap<String, String> response = given().port(port)
                .contentType("application/json")
                .body("{\"username\":\"" + user0.getUsername() + "\"," +
                        "\"password\":\"" + user0.getPassword() + "\"}")
                .when()
                .post("/patient/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .as(HashMap.class);

        loginToken = response.get("token");

        // now, post the medic to use for appointments

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .post("/staff/medics?name=Dr.Bananas&specialities=CARDIOLOGY,DERMATOLOGY,ENDOCRINOLOGY")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("When post a Appointment return a Appointment")
    void whenPostValidAppointment_thenCreateAppointment() throws Exception {

        AppointmentSchema app0 = new AppointmentSchema(
                new Date().getTime(),
                50.0,
                Speciality.CARDIOLOGY,
                1L);

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .body(app0)
                .when()
                .post("/patient/appointments")
                .then()
                .statusCode(201)
                .assertThat()
                .body("patient.username", equalTo(user0.getUsername()))
                .body("medic.name", equalTo(medic0.getName()));

        /*
         * 
         * given().port(port)
         * .contentType("application/json")
         * .header(new Header("Authorization", "Bearer " + loginToken))
         * .body("{\"date\": \"1714159929001\", \"price\": \"" + app0.getPrice() +
         * "\", \"specialityId\": \"" + 1 + "\" "+ "\", \"medicId\": \"" + 1 + "\"}")
         * .when()
         * .when()
         * .post("/appointments")
         * .then()
         * .statusCode(201);
         * 
         * given().port(port)
         * .contentType("application/json")
         * .header(new Header("Authorization", "Bearer " + loginToken))
         * .when()
         * .get("/appointments")
         * .then().log().all()
         * .statusCode(200)
         * .assertThat().
         * body("$.size()", equalTo(2)).
         * body("[0].price", equalTo(app0.getPrice().floatValue())).
         * body("[1].price", equalTo(app1.getPrice().floatValue())).
         * body("[0].date", equalTo("1714159929000")).
         * body("[1].date", equalTo("1714159929001")).
         * extract().as(Appointment[].class);
         * 
         * given().port(port)
         * .contentType("application/json")
         * .header(new Header("Authorization", "Bearer " + loginToken))
         * .when()
         * .get("/appointments/" + app0Saved.get("id"))
         * .then().log().all()
         * .statusCode(200)
         * .assertThat().
         * body("price", equalTo(app0.getPrice().floatValue())).
         * body("date", equalTo("1714159929000")).
         * extract().as(Appointment.class);
         */
    }

}