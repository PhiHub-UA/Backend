package deti.tqs.phihub.integrationTests;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.User;

import io.restassured.RestAssured;
import io.restassured.http.Header;

import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

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
        private static String staffToken;

        @BeforeAll
        void setUp() {

                RestAssured.baseURI = BASE_URI;
                RestAssured.port = port;

                // Create a user
                user0.setId(1L);
                user0.setUsername("zezocas");
                user0.setEmail("josefino123@gmail.com");
                user0.setPhone("987654321");
                user0.setAge(27);
                user0.setPassword("strongPassword");

                // Create a medic
                medic0.setId(1L);
                medic0.setName("Dr.Bananas");
                medic0.setPassword("banan");
                medic0.setSpecialities(List.of(Speciality.CARDIOLOGY, Speciality.DERMATOLOGY, Speciality.ENDOCRINOLOGY));

                given().port(port)
                                .contentType("application/json")
                                .body("{"
                                        + "\"phone\":\"" + user0.getPhone() + "\","
                                        + "\"email\":\"" + user0.getEmail() + "\","
                                        + "\"age\":\"" + user0.getAge() + "\","
                                        + "\"username\":\"" + user0.getUsername() + "\","
                                        + "\"password\":\"" + user0.getPassword() + "\","
                                        + "\"name\":\"" + user0.getUsername() + "\","
                                        + "\"permissions\":[],"
                                        + "\"role\":\"staff\""
                                        + "}")
                                .when()
                                .post("/auth/register")
                                .then()
                                .statusCode(201);

                HashMap<String, String> response = given().port(port)
                                .contentType("application/json")
                                .body("{\"username\":\"" + user0.getUsername() + "\"," +
                                        "\"password\":\"" + user0.getPassword() + "\"," +
                                        "\"role\":\"staff\"}")
                                .when()
                                .post("/auth/login")
                                .then()
                                .statusCode(200)
                                .extract()
                                .as(HashMap.class);

                staffToken = response.get("token");

                // now, post the medic to use for appointments
                String specialityArrays = "[";
                for (Speciality spec : medic0.getSpecialities()) {
                        specialityArrays += "\"" + spec + "\",";
                }
                specialityArrays = specialityArrays.substring(0, specialityArrays.length() - 1);
                specialityArrays += "]";

                given().port(port)
                                .contentType("application/json")
                                .header(new Header("Authorization", "Bearer " + staffToken))
                                .when()
                                .body("{\"name\":\"" + medic0.getName()
                                        + "\",\"specialities\":" + specialityArrays
                                        + ",\"password\":\"" + medic0.getPassword()
                                        + "\"}")
                                .post("/staff/medics")
                                .then()
                                .statusCode(201);

                // register and login a user to make appointments
                given().port(port)
                                .contentType("application/json")
                                .body("{"
                                        + "\"phone\":\"" + user0.getPhone() + "\","
                                        + "\"email\":\"" + user0.getEmail() + "\","
                                        + "\"age\":\"" + user0.getAge() + "\","
                                        + "\"username\":\"" + user0.getUsername() + "\","
                                        + "\"password\":\"" + user0.getPassword() + "\","
                                        + "\"name\":\"" + user0.getUsername() + "\","
                                        + "\"role\":\"user\""
                                        + "}")
                                .when()
                                .post("/auth/register")
                                .then()
                                .statusCode(201);

                given().port(port)
                                .contentType("application/json")
                                .body("{\"username\":\"" + user0.getUsername() + "\"," +
                                        "\"password\":\"" + user0.getPassword() + "\"," +
                                        "\"role\":\"user\"}")
                                .when()
                                .post("/auth/login")
                                .then()
                                .statusCode(200)
                                .extract()
                                .as(HashMap.class);

        }
}