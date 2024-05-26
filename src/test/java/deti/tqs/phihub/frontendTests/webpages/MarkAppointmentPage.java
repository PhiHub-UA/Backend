package deti.tqs.phihub.frontendTests.webpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import deti.tqs.phihub.configs.Generated;
import org.openqa.selenium.By;

@Generated
public class MarkAppointmentPage {
    private WebDriver driver;

    //  Page URL
    private static String PAGE_URL="http://localhost:3001/appointments";

    //  Locators
    @FindBy(id = "selectSpeciality")
    private WebElement specialitySelect;

    @FindBy(id = "selectDoctor")
    private WebElement doctorSelect;

    @FindBy(id = "selectHour")
    private WebElement hourSelect;

    @FindBy(id = "goToStep2Btn")
    private WebElement step2Button;

    @FindBy(id = "goToStep3Btn")
    private WebElement step3Button;

    @FindBy(xpath = "/html/body/div/main/main/div[1]/div[2]/div/div[4]/div[1]/div/div/div/table/tbody/tr[5]/td[1]/button")
    private WebElement selectDateButton;

    @FindBy(id = "markButton")
    private WebElement markButton;

    @FindBy(id = "confirmationText")
    private WebElement confirmationText;
    

    //  Constructor
    public MarkAppointmentPage(WebDriver ndriver) {
        driver=ndriver;
        driver.get(PAGE_URL);

        //  Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void chooseSpeciality() {
        specialitySelect.click();
        WebElement cardioBtn = driver.findElement(By.id("CARDIOLOGY"));
        cardioBtn.click();  
    }

    public void goToChooseMedicStep() {
        step2Button.click();
    }

    public void chooseDoctor() {
        doctorSelect.click();
        WebElement josefinoBtn = driver.findElement(By.id("1"));
        josefinoBtn.click();  
    }

    public void goToChooseDateStep() {
        step3Button.click();
    }

    public void pickADate() {
        selectDateButton.click();
    }

    public void chooseHour() {
        hourSelect.click();
        WebElement hour9Btn = driver.findElement(By.id("09:00"));
        hour9Btn.click();
    }

    public void markAppointment() {
        markButton.click();
    }

    public String getConfirmationTextAppointment() {
        WebElement headerText = driver.findElement(By.id("confirmationText"));
        return headerText.getText();
    }
}
