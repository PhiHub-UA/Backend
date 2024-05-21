package deti.tqs.phihub.frontendTests.webpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import java.time.Duration;

public class HomePage {
    private WebDriver driver;

    //Page URL
    private static String PAGE_URL="file:///home/frostywolf/Documents/GitReps/TQS_107348/HW1/frontend/src/index.html";
    //private static String PAGE_URL="https://localhost:3000/index.html";

    //Locators
    @FindBy(id = "origin")
    private WebElement originSelectBox;

    @FindBy(id = "destination")
    private WebElement destinationSelectBox;

    @FindBy(id = "submitbtn")
    private WebElement findTripsButton;

    //Constructor
    public HomePage(WebDriver ndriver){
        driver=ndriver;
        driver.get(PAGE_URL);
        //Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void clickOnSearchTripsButton(){
        findTripsButton.click();
    }

    public void selectOnOriginSelectBox(Integer index){
        Select drop = new Select(originSelectBox);
        drop.selectByIndex(index);  
    }

    public void selectOnRouteSelectBox(Integer index){
        Select drop = new Select(destinationSelectBox);
        drop.selectByIndex(index);  
    }

    public String getHeaderText(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        

        WebElement headerText = driver.findElement(By.id("headertext"));
        return headerText.getText();  
    }
}
