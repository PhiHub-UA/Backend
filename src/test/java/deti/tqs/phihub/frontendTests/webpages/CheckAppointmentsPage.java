package deti.tqs.phihub.frontendTests.webpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import deti.tqs.phihub.configs.Generated;
import org.openqa.selenium.By;

@Generated
public class CheckAppointmentsPage {
    private WebDriver driver;

    //  Page URL
    private static String PAGE_URL="http://localhost:3001/appointments";

    //  Locators
    @FindBy(id = "confirmationText")
    private WebElement confirmationText;
    

    //  Constructor
    public CheckAppointmentsPage(WebDriver ndriver) {
        driver=ndriver;
        driver.get(PAGE_URL);

        //  Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public String getConfirmationTextAppointment() {
        WebElement headerText = driver.findElement(By.id("uname"));
        return headerText.getText();
    }
}
