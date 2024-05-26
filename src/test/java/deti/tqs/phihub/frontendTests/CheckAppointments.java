package deti.tqs.phihub.frontendTests;


import deti.tqs.phihub.frontendTests.webpages.CheckAppointmentsPage;
import deti.tqs.phihub.frontendTests.webpages.HomePage;
import deti.tqs.phihub.frontendTests.webpages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.time.Duration;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@ExtendWith(SeleniumJupiter.class)
public class CheckAppointments {

    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    private CheckAppointmentsPage checkAppointmentsPage;

    @Given("User is on the check appointments page")
    public void userEntersFrontend() {

        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();

        driver.manage().window().maximize();

        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        checkAppointmentsPage = new CheckAppointmentsPage(driver);
    }


    @When("User is logged in as a patient with name {string} and password {string}")
    public void userInsertsHisName(String username, String password)  {

        homePage.clickOnLogin();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        loginPage.clickOnLoginTab();
        loginPage.setLoginUsername(username);
        loginPage.setLoginPassword(password);
        loginPage.clickOnLoginButton();
    }

    @And("User clicks the Check Appointments button")
    public void userClicksOnLogin()  {
        driver.navigate().refresh();
        homePage.clickCheckAppointments();
    }

    @Then("User should see his appointments")
    public void userGoesToTrips() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        String successText = checkAppointmentsPage.getConfirmationTextAppointment();
        assertEquals("jose_fino", successText);

        driver.close();
    }
}