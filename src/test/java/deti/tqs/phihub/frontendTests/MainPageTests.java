package deti.tqs.phihub.frontendTests;


import deti.tqs.phihub.frontendTests.webpages.HomePage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.time.Duration;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bonigarcia.seljup.SeleniumJupiter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


@ExtendWith(SeleniumJupiter.class)
public class MainPageTests {

    private WebDriver driver;
    private HomePage homePage;

    @Given("the user accessed the frontend")
    public void userEntersFrontend() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();

        homePage = new HomePage(driver);
    }

    @When("the user selects the origin with index {int}")
    public void userSelectsOrigin(Integer originCity)  {
        homePage.selectOnOriginSelectBox(originCity);
    }

    @And("selects the route with index {int}")
    public void userSelectsDestination(Integer routeID)  {
        homePage.selectOnRouteSelectBox(routeID);
    }

    @And("the user presses the search button")
    public void userPressesSearch()  {
        homePage.clickOnSearchTripsButton();
    }

    @Then("the user should go to the trips list page")
    public void userGoesToTrips() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        String headerText = homePage.getHeaderText();
        assertEquals("Trips from Aveiro to Leiria", headerText);

        driver.close();
    }
    
}