package deti.tqs.phihub.frontendTests;


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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;


@ExtendWith(SeleniumJupiter.class)
public class RegisterTests {

    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;

    @Given("User is in the website")
    public void userEntersFrontend() {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
    }

    @When("User clicks the Login button")
    public void userClicksOnLogin()  {
        homePage.clickOnLogin();
    }

    @And("The user inserts his name as {string}")
    public void userInsertsHisName(String name)  {
        loginPage.setName(name);
    }

    @And("The user inserts his username as {string}")
    public void userInsertsHisUsername(String username)  {
        loginPage.setUsername(username);
    }

    @And("The user inserts his password as {string}")
    public void userInsertsHisPassword(String password)  {
        loginPage.setPassword(password);
    }

    @And("The user inserts his phone as {int}")
    public void userInsertsHisPhone(Integer phone)  {
        loginPage.setPhone(phone);
    }

    @And("The user inserts his email as {string}")
    public void userInsertsHisEmail(String email)  {
        loginPage.setEmail(email);
    }

    @And("The user inserts his age as {int}")
    public void userInsertsHisAge(Integer age)  {
        loginPage.setAge(age);
    }

    @And("The user clicks on the Register button")
    public void userClicksOnRegister()  {
        loginPage.clickOnRegisterButton();
    }

    @Then("User should see the register confirmation")
    public void userGoesToTrips() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        String headerText = loginPage.getConfirmationTextRegister();
        assertEquals("Successfully registered", headerText);

        driver.close();
    }
    
}