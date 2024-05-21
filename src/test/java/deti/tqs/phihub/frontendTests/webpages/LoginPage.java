package deti.tqs.phihub.frontendTests.webpages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.By;

public class LoginPage {
    private WebDriver driver;

    //  Page URL
    private static String PAGE_URL="http://localhost:3001/login";

    //  Locators
    @FindBy(id = ":r7:-form-item")
    private WebElement nameInput;
    
    @FindBy(id = ":r9:-form-item")
    private WebElement usernameInput;

    @FindBy(id = ":rb:-form-item")
    private WebElement passwordInput;
    
    @FindBy(id = ":rd:-form-item")
    private WebElement phoneInput;
    
    @FindBy(id = ":rf:-form-item")
    private WebElement emailInput;
    
    @FindBy(id = ":rh:-form-item")
    private WebElement ageInput;
    
    @FindBy(id = ":rl:-form-item")
    private WebElement loginUsernameInput;
    
    @FindBy(id = ":rn:-form-item")
    private WebElement loginPasswordInput;

    @FindBy(id = "loginTab")
    private WebElement loginTabButton;
    
    @FindBy(id = "registerBtn")
    private WebElement registerButton;
    

    //  Constructor
    public LoginPage(WebDriver ndriver){
        driver=ndriver;
        driver.get(PAGE_URL);

        //  Initialise Elements
        PageFactory.initElements(driver, this);
    }

    public void clickOnRegisterButton(){
        registerButton.click();
    }

    public void clickOnLoginButton(){
        WebElement loginButton = driver.findElement(By.id("loginBtn"));

        loginButton.click();
    }

    public void clickOnLoginTab(){
        loginTabButton.click();
    }

    public String getConfirmationTextRegister(){
        WebElement headerText = driver.findElement(By.id("successTextRegister"));
        return headerText.getText();  
    }

    public String getConfirmationTextLogin(){
        WebElement headerText = driver.findElement(By.id("mainText"));
        return headerText.getText();  
    }

    public void setName(String name){
        nameInput.clear();
        nameInput.sendKeys(name);
    }

    public void setUsername(String username){
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }
    
    public void setPassword(String pass){
        passwordInput.clear();
        passwordInput.sendKeys(pass);
    }
    
    public void setPhone(Integer ph){
        phoneInput.clear();
        phoneInput.sendKeys(ph.toString());
    }
    
    public void setEmail(String mail){
        emailInput.clear();
        emailInput.sendKeys(mail);
    }
    
    public void setAge(Integer age){
        ageInput.clear();
        ageInput.sendKeys(age.toString());
    }
    
    public void setLoginUsername(String username){
        loginUsernameInput.clear();
        loginUsernameInput.sendKeys(username.toString());
    }
    
    public void setLoginPassword(String pass){
        loginPasswordInput.clear();
        loginPasswordInput.sendKeys(pass.toString());
    }
}
