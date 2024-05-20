package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.dtos.StaffSchema;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.models.StaffPermissions;
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
class StaffIntegrationTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    private String loginToken;
    private Staff staff0 = new Staff();
    StaffSchema staff0Schema;

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;

        // Create a staff
        staff0.setId(10L);
        staff0.setUsername("rodrigues33");
        staff0.setEmail("rodr@fino.com");
        staff0.setPhone("919828737");
        staff0.setAge(27);
        staff0.setPassword("strongPasswordR");
        staff0Schema = new StaffSchema("0", staff0.getEmail(), staff0.getAge(), staff0.getUsername(), "josestaff", staff0.getPassword(), List.of());

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
                .when()
                .get("/staff/me")
                .then()
                .statusCode(200)
                .assertThat().body("username", equalTo(staff0.getUsername())).body("phone", equalTo(staff0.getPhone()))
                .extract().as(Staff.class);

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .body(staff0Schema)
                .when()
                .get("/staff")
                .then()
                .statusCode(200)
                .assertThat().body("[2].username", equalTo(staff0.getUsername())).body("[1].age", equalTo(staff0.getAge()));
                
        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .body(staff0Schema)
                .when()
                .get("/staff/permissions")
                .then()
                .statusCode(200)
                .assertThat().body("[0]", equalTo(StaffPermissions.getPermissions().get(0)));

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .body(staff0Schema)
                .when()
                .post("/staff")
                .then()
                .statusCode(200)
                .assertThat().body("username", equalTo(staff0.getUsername())).body("age", equalTo(staff0.getAge()))
                .extract().as(Staff.class);
    }
}