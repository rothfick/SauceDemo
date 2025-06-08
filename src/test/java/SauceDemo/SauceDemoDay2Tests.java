package SauceDemo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class SauceDemoDay2Tests {
    private WebDriver driver;
    private WebDriverWait wait;
    // NIE WOLNO UZYWAC THREAD.SLEEP() na produkcji/stg/sandboxach/int/uat itd itd !!!!!!!!

    public static final String STANDARD_USER="standard_user";
    public static final String LOCKED_USER="locked_out_user";
    public static final String PRObLEM_USER="problem_user";
    public static final String PERFORMANCE_USER="performance_user";
    public static final String PASSWORD =  "secret_sauce";

    //setup i teardown

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void standardUserLoginTest(){
        System.out.println("===== TEST STANDARD USER =====");
        loginUser(STANDARD_USER, PASSWORD);
        //Verification of correct logon
        String url = driver.getCurrentUrl();
        String expectedUrl = "https://www.saucedemo.com/inventory.html";
        Assert.assertEquals(url, expectedUrl, "Wrong url -> We are expecting " + expectedUrl);

    }

    private void loginUser(String user, String password) {
        System.out.println("===== LOGIN WITH " + user +  " USER =====");
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        usernameField.clear();
        usernameField.sendKeys(user);
        passwordField.clear();
        passwordField.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }


}
