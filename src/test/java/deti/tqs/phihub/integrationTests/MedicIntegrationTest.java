package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.models.Medic;
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
class MedicIntegrationTests {

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
        user0.setUsername("zezocas123");
        user0.setEmail("josefino123@gmail.com");
        user0.setPhone("987654321");
        user0.setRole("admin");
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

        // now, post the medic to use for medics

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .post("/staff/medics?name=" + medic0.getName() + "&specialities=CARDIOLOGY,DERMATOLOGY,ENDOCRINOLOGY")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("When post a Medic return a Medic")
    void whenGetValidMedic_thenReturnMedic() throws Exception {

        //  Test with a given speciality
        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .get("/patient/medics?speciality" + medic0.getSpecialities().get(0).toString())
                .then()
                .statusCode(200)
                .assertThat()
                .body("[0].name", equalTo(medic0.getName()));

        //  Test with no given speciality
        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .get("/patient/medics")
                .then()
                .statusCode(200)
                .assertThat()
                .body("[0].name", equalTo(medic0.getName()));

        //  Test the availability
        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .get("/patient/medics/availability/" + medic0.getId().toString() + "?day=1714402800")
                .then()
                .statusCode(200)
                .assertThat()
                .body("[0]", equalTo("09:00"));
    }
}