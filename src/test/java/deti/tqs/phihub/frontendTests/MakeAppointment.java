package deti.tqs.phihub.frontendTests;


import deti.tqs.phihub.frontendTests.webpages.HomePage;
import deti.tqs.phihub.frontendTests.webpages.LoginPage;
import deti.tqs.phihub.frontendTests.webpages.MarkAppointmentPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.time.Duration;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@ExtendWith(SeleniumJupiter.class)
public class MakeAppointment {

    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;
    private MarkAppointmentPage markAppointmentPage;

    @Given("I am on Phihub Patient Website")
    public void userEntersFrontend() {

        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();

        driver.manage().window().maximize();

        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        markAppointmentPage = new MarkAppointmentPage(driver);
    }

    @When("I am logged in as a patient with name {string} and password {string}")
    public void userInsertsHisName(String username, String password)  {

        homePage.clickOnLogin();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        loginPage.clickOnLoginTab();
        loginPage.setLoginUsername(username);
        loginPage.setLoginPassword(password);
        loginPage.clickOnLoginButton();
    }

    @And("I click on the Make an appointment button")
    public void userClicksOnLogin()  {
        driver.navigate().refresh();
        homePage.clickMarkAppointment();
    }

    @And("I select which speciality I want to make an appointment in")
    public void userInsertsHisUsername()  {
        markAppointmentPage.chooseSpeciality();
        markAppointmentPage.goToChooseMedicStep();
    }

    @And("I select which doctor I want to make an appointment with")
    public void userInsertsHisPassword()  {
        markAppointmentPage.chooseDoctor();
        markAppointmentPage.goToChooseDateStep();
    }

    @And("I select the date and time I want to make an appointment")
    public void userInsertsHisPhone()  {
        markAppointmentPage.pickADate();
        markAppointmentPage.chooseHour();
    }

    @And("I click on the confirm button")
    public void userInsertsHisEmail()  {
        markAppointmentPage.markAppointment();
    }

    @Then("I should see a confirmation message")
    public void userGoesToTrips() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        String successText = markAppointmentPage.getConfirmationTextAppointment();
        assertEquals("Appointment marked successfully", successText);

        driver.close();
    }
    
}