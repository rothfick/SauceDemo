package SauceDemo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class SauceDemoDay2Tests {
    private WebDriver driver;
    private WebDriverWait wait;
    // NIE WOLNO UZYWAC THREAD.SLEEP() na produkcji/stg/sandboxach/int/uat itd itd !!!!!!!!

    public static final String STANDARD_USER="standard_user";
    public static final String LOCKED_USER="locked_out_user";
    public static final String PRObLEM_USER="problem_user";
    public static final String PERFORMANCE_USER="performance_user";
    public static final String PASSWORD =  "secret_sauce";

    //WebElement element = driver....
    //element.getText();
//    Assert.assertEquals();
//    By.xpath("//*[@id="login_button_container"]/div/form/div[3]/h3/text()");

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

    @Test
    public void lockedUserLoginTest() throws InterruptedException {
        System.out.println("===== TEST LOCKED USER =====");
        loginUser(LOCKED_USER, PASSWORD);

        String errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
        Assert.assertEquals(errorMessage, "Epic sadface: Sorry, this user has been locked out.", "Wrong error message -> We are expecting Invalid Credentials");
    }

    @Test
    public void productBrowsingTest(){
        System.out.println("===== TEST PRODUCT BROWSING =====");

        loginUser(STANDARD_USER, PASSWORD);


        //Pobierz wszystkie produkty
        List<WebElement>  products = driver.findElements(By.className("inventory_item"));
        System.out.println(products.size());

        for(int i = 0; i< products.size(); i++){
            WebElement product = products.get(i);
            String name = product.findElement(By.className("inventory_item_name")).getText();
            String description = product.findElement(By.className("inventory_item_desc")).getText();
            String price = product.findElement(By.className("inventory_item_price")).getText();

            System.out.println("\n---- Produkt " + (i + 1) + " ====");
            System.out.println("Name: " + name);
            System.out.println("Description: " + description);
            System.out.println("Price: " + price);

            Assert.assertNotNull(name, "Name is null");
            Assert.assertNotNull(description, "Description is null");
            Assert.assertNotNull(price, "Price is null");
        }


    }

    @Test
    public void addToCartTest() throws InterruptedException {
        System.out.println("===== TEST ADD TO CART =====");
        loginUser(STANDARD_USER, PASSWORD);
// SOLUTION 1
//        WebElement firstProduct = driver.findElement(By.className("inventory_item"));
//        String productName = firstProduct.findElement(By.className("inventory_item_name")).getText();
//
//        WebElement addToCartButton = firstProduct.findElement(By.cssSelector("#add-to-cart-sauce-labs-backpack"));
//        String originalButtonText = addToCartButton.getText();
//
//        System.out.println("Dodajemy do koszyka " + productName);
//        addToCartButton.click();
//
//        //sprawdz czy przycisk sie zmienil
//
//        WebElement refreshedProduct = driver.findElement(By.className("inventory_item"));
//        WebElement refreshedButton = refreshedProduct.findElement(By.cssSelector("#remove-sauce-labs-backpack"));
//
//        String newButtonText = refreshedButton.getText();
//        Assert.assertNotEquals(newButtonText, originalButtonText, "New button is not equal to original button text")


        //SOLUTION 2
//        By firstProductLocator = By.className("inventory_item");
//        By productNameLocator = By.className("inventory_item_name");
//        By addButtonLocator = By.cssSelector("#add-to-cart-sauce-labs-backpack");
//
//        String productName = driver.findElement(firstProductLocator).findElement(productNameLocator).getText();
//
//        System.out.println("\n---- Add To Cart ----" + productName);
//
//        driver.findElement(firstProductLocator).findElement(addButtonLocator).click();
//
//        By removeButtonLoctor = By.cssSelector("#remove-from-cart-sauce-labs-backpack");
//        String newButtonText = driver.findElement(firstProductLocator).findElement(removeButtonLoctor).getText();
//

        //SOLUTION 3 - CONDITIONY
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement firstProduct = driver.findElement(By.className("inventory_item"));
        String productName = firstProduct.findElement(By.className("inventory_item_name")).getText();

        System.out.println("\n---- Adding to cart ====" + productName);
        WebElement addButton = firstProduct.findElement(By.cssSelector("#add-to-cart-sauce-labs-backpack"));
        addButton.click();

        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(
                firstProduct.findElement(By.cssSelector("#remove-sauce-labs-backpack"))));

        String newButtonText = removeButton.getText();

        WebElement cartBadge = driver.findElement(By.className("shopping_cart_badge"));
        Assert.assertEquals(cartBadge.getText(), "1", "Badge text is wrong");

    }


    @Test
    public void Test(){
        loginUser(STANDARD_USER, PASSWORD);
        inventoryPage();
        String shopCardNumber = driver.findElement(By.className("shopping_cart_badge")).getText();
        System.out.println(shopCardNumber);
        Assert.assertEquals(shopCardNumber, "2", "Wrong ShopCardNumber");



    }
    private void inventoryPage() {
        WebElement sauceLabsBackPackAddToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        sauceLabsBackPackAddToCartButton.click();
        WebElement sauceLabsBikeLightAddToCartButton = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light"));
        sauceLabsBikeLightAddToCartButton.click();

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
