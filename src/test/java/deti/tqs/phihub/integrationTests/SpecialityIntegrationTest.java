package deti.tqs.phihub.integrationTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import deti.tqs.phihub.models.User;

import io.restassured.RestAssured;
import io.restassured.http.Header;

import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.HashMap;


import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class SpecialityIntegrationTest {
    
    private final static String BASE_URI = "http://localhost";
 
    @LocalServerPort
    private int port;

    private String loginToken;
    private User user0 = new User();

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;

        //  Create a user
        user0.setId(1L);
        user0.setUsername("rodrigues123");
        user0.setEmail("joana@fino.com");
        user0.setPhone("919828737");
        user0.setAge(27);
        user0.setPassword("strongPassword");

        given().port(port)
            .contentType("application/json")
            .body("{\"phone\":\"" + user0.getPhone() + "\"," +
                    "\"email\":\"" + user0.getEmail() + "\"," +
                    "\"age\":\"" + user0.getAge() + "\"," +
                    "\"username\":\"" + user0.getUsername() + "\"," +
                    "\"password\":\"" + user0.getPassword() + "\"}")
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
    @DisplayName("When post a Login return a Login")
    void whenPostValidLogin_thenCreateLogin() {

        List<String> response = given().port(port)
            .contentType("application/json")
            .header(new Header("Authorization", "Bearer " + loginToken))
            .when()
            .get("/patient/speciality")
            .then()
            .statusCode(200)
            .extract()
            .as(List.class);

        assertNotEquals(null, response);
    }
}