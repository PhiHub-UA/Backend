package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.dtos.MedicSchema;
import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.Staff;
import io.restassured.RestAssured;
import io.restassured.http.Header;

import static org.hamcrest.Matchers.*;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class StaffMedicIntegrationTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    private String loginToken;
    private Staff staff0 = new Staff();
    private Medic medic0 = new Medic();
    MedicSchema medicSchema;

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;

        //  Create a staff
        staff0.setId(10L);
        staff0.setUsername("rodrigues3");
        staff0.setEmail("joana@fino.com");
        staff0.setPhone("919828737");
        staff0.setAge(27);
        staff0.setPassword("strongPassword");

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

    }

    @Test
    @DisplayName("When post a Staff return a Staff")
    void whenPostValidStaff_thenCreateStaff() {

        given().port(port)
                .contentType("application/json")
                .body("{"
                        + "\"phone\":\"" + staff0.getPhone() + "\","
                        + "\"email\":\"" + staff0.getEmail() + "\","
                        + "\"age\":\"" + staff0.getAge() + "\","
                        + "\"username\":\"" + staff0.getUsername() + "\","
                        + "\"password\":\"" + staff0.getPassword() + "\","
                        + "\"name\":\"" + staff0.getUsername() + "\","
                        + "\"permissions\":[],"
                        + "\"role\":\"staff\""
                        + "}")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        HashMap<String, String> response = given().port(port)
                .contentType("application/json")
                .body("{\"username\":\"" + staff0.getUsername() + "\"," +
                        "\"password\":\"" + staff0.getPassword() + "\"," +
                        "\"role\":\"staff\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .as(HashMap.class);

        loginToken = response.get("token");

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .body(medicSchema)
                .when()
                .post("/staff/medics")
                .then()
                .statusCode(201)
                .assertThat().body("username", equalTo(medic0.getUsername())).body("age", equalTo(medic0.getAge()));

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .get("/staff/medics")
                .then()
                .statusCode(200)
                .assertThat().body("[4].username", equalTo(medic0.getUsername())).body("[4].phone", equalTo(medic0.getPhone()));

        response = given().port(port)
                .contentType("application/json")
                .body("{\"username\":\"" + medic0.getUsername() + "\"," +
                        "\"password\":\"" + medic0.getPassword() + "\"," +
                        "\"role\":\"medic\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .as(HashMap.class);

        loginToken = response.get("token");

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .get("/staff/medics/me")
                .then()
                .statusCode(200)
                .assertThat().body("username", equalTo(medic0.getUsername())).body("phone", equalTo(medic0.getPhone()));
    }
}