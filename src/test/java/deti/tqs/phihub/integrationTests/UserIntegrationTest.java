package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.models.User;

import io.restassured.RestAssured;
import io.restassured.http.Header;

import static org.hamcrest.Matchers.*;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.HashMap;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@TestInstance(Lifecycle.PER_CLASS)
class UserIntegrationTests {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    private String loginToken;
    private User user0 = new User();

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;

        // Create a user
        user0.setId(10L);
        user0.setUsername("rodrigues");
        user0.setEmail("joana@fino.com");
        user0.setPhone("919828737");
        user0.setAge(27);
        user0.setPassword("strongPassword");
    }

    @Test
    @DisplayName("When post a User return a User")
    void whenPostValidUser_thenCreateUser() {

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
                        + "\"role\":\"user\""
                        + "}")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201);

        HashMap<String, String> response = given().port(port)
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

        loginToken = response.get("token");

        User user = given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .get("/patient/users/me")
                .then()
                .statusCode(200)
                .assertThat().body("username", equalTo(user0.getUsername())).body("phone", equalTo(user0.getPhone()))
                .extract().as(User.class);

        given().port(port)
                .contentType("application/json")
                .header(new Header("Authorization", "Bearer " + loginToken))
                .when()
                .get("/patient/users/" + user.getId())
                .then()
                .statusCode(200)
                .assertThat().body("username", equalTo(user0.getUsername())).body("phone", equalTo(user0.getPhone()))
                .extract().as(User.class);
    }
}