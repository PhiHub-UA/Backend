package deti.tqs.phihub.frontendTests.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private WebDriver driver;

    //Page URL
    private static String PAGE_URL="http://localhost:3001/";

    //Locators
    @FindBy(id = "loginBtnNav")
    private WebElement loginButton;

    //Constructor
    public HomePage(WebDriver ndriver){
        driver=ndriver;
        driver.get(PAGE_URL);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void clickOnLogin() {
        WebDriverWait w1 = new WebDriverWait(driver, Duration.ofSeconds(2));
        w1.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginBtnNav")));

        loginButton.click();
    }
}
