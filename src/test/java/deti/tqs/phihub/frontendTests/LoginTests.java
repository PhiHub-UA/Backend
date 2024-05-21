package deti.tqs.phihub.frontendTests;


import deti.tqs.phihub.frontendTests.webpages.LoginPage;
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
public class LoginTests {

    private WebDriver driver;
    private LoginPage loginPage;

    @Given("User is in the login page")
    public void userEntersFrontend() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
    }

    @When("User clicks the Login tab")
    public void userClicksOnLoginTab()  {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        loginPage.clickOnLoginTab();
    }

    @And("The user inserts his account username as {string}")
    public void userInsertsHisUsername(String username)  {
        loginPage.setLoginUsername(username);
    }

    @And("The user inserts his account password as {string}")
    public void userInsertsHisPassword(String password)  {
        loginPage.setLoginPassword(password);
    }

    @And("The user clicks on the Login Button")
    public void userClicksOnLogin()  {
        loginPage.clickOnLoginButton();
    }

    @Then("User should see the login confirmation")
    public void userGoesToTrips() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        String headerText = loginPage.getConfirmationTextLogin();
        assertEquals("PhiHub Patient Office", headerText);

        driver.close();
    }
}