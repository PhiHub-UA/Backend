package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.services.SpecialityService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.http.Header;

import static org.hamcrest.Matchers.*;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Date;
import java.util.HashMap;
import java.util.Arrays;
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

    private static int currPort;

    private static String loginToken;
    private static Appointment app0 = new Appointment();
    private static User user0 = new User();

    @BeforeEach
    public void setUpPort() throws Exception {
        System.out.println("1--------------------------------------------\n" + loginToken);
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        currPort = port;
    }

    @BeforeAll
    public static void setUp() throws Exception {

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
        app0.setDate(new Date());
        app0.setPrice(12.3);
        app0.setPatient(user0);


        System.out.println("1--------------------------------------------\n" + loginToken);
        given().port(currPort)
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

            System.out.println("1--------------------------------------------\n" + loginToken);
        HashMap<String, String> response = given().port(currPort)
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
        System.out.println("1--------------------------------------------\n" + loginToken);

    }

    @Test
    @DisplayName("When post a Appointment return a Appointment")
    void whenPostValidAppointment_thenCreateAppointment() throws Exception {

        given().port(port)
            .contentType("application/json")
            .header(new Header("Authorization", "Bearer " + loginToken))
            .body("{\"date\": \"2024-04-26T18:32:09\", \"price\": \"" + app0.getPrice() + "\", \"specialityId\": \"" + 1 + "\"}")
            .when()
            .post("/appointments")
            .then()
            .statusCode(201)
            .assertThat()
            .body("date", is("2024-04-26T18:32:09.000+00:00"));
        System.out.println("2--------------------------------------------\n" + loginToken);
    }

    @Test
    @DisplayName("When list Appointments get Appointments")
    void whenGetAppointments_thenGetAppointments() throws Exception {

        Appointment app1 = new Appointment();

        app1.setId(2L);
        app1.setDate(new Date());
        app1.setPrice(24.7);
        app1.setPatient(user0);
        System.out.println("3--------------------------------------------\n" + loginToken);


        given().port(port)
            .contentType("application/json")
            .header(new Header("Authorization", "Bearer " + loginToken))
            .body("{\"date\": \"2024-04-26T18:32:09\", \"price\": \"" + app0.getPrice() + "\", \"specialityId\": \"" + 1 + "\"}")
            .when()
            .post("/appointments")
            .then()
            .statusCode(201);

            System.out.println("4--------------------------------------------\n" + loginToken);

        given().port(port)
            .contentType("application/json")
            .header(new Header("Authorization", "Bearer " + loginToken))
            .body("{\"date\": \"2025-11-22T23:14:17\", \"price\": \"" + app1.getPrice() + "\", \"specialityId\": \"" + 1 + "\"}")
            .when()
            .post("/appointments")
            .then()
            .statusCode(201);

            System.out.println("5--------------------------------------------\n" + loginToken);
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
                body("[0].date", equalTo("2024-04-26T18:32:09.000+00:00")).
                body("[1].date", equalTo("2025-11-22T23:14:17.000+00:00")).
            extract().as(Appointment[].class);
    }
}