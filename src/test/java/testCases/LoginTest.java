package testCases;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Login flow against saucedemo.com, a stable public demo e-commerce site.
 *
 * By default runs against a local headless browser (no infrastructure needed,
 * used by CI). Pass -DgridUrl=http://localhost:4444/wd/hub to instead route
 * through the Selenium Grid started by docker-compose.yml, which is how this
 * project demonstrates parallel cross-browser execution across chrome,
 * firefox and edge nodes (see testng-grid.xml).
 */
public class LoginTest {

    private WebDriver driver;

    @BeforeTest
    @Parameters({"browser"})
    void setup(@Optional("chrome") String browser) throws MalformedURLException {
        String gridUrl = System.getProperty("gridUrl");

        if (gridUrl != null && !gridUrl.isBlank()) {
            driver = new RemoteWebDriver(new URL(gridUrl), optionsFor(browser, false));
        } else {
            driver = switch (browser) {
                case "firefox" -> new FirefoxDriver((FirefoxOptions) optionsFor(browser, true));
                case "edge" -> new EdgeDriver((EdgeOptions) optionsFor(browser, true));
                default -> new ChromeDriver((ChromeOptions) optionsFor(browser, true));
            };
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
    }

    private Capabilities optionsFor(String browser, boolean headless) {
        return switch (browser) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                yield options;
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) options.addArguments("--headless=new");
                yield options;
            }
            default -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                yield options;
            }
        };
    }

    @Test
    void standardUserCanLogInAndSeeInventory() {
        driver.get("https://www.saucedemo.com/");

        driver.findElement(By.cssSelector("[data-test='username']")).sendKeys("standard_user");
        driver.findElement(By.cssSelector("[data-test='password']")).sendKeys("secret_sauce");
        driver.findElement(By.cssSelector("[data-test='login-button']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));

        String pageTitle = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(pageTitle, "Products");
    }

    @Test
    void lockedOutUserSeesErrorMessage() {
        driver.get("https://www.saucedemo.com/");

        driver.findElement(By.cssSelector("[data-test='username']")).sendKeys("locked_out_user");
        driver.findElement(By.cssSelector("[data-test='password']")).sendKeys("secret_sauce");
        driver.findElement(By.cssSelector("[data-test='login-button']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));

        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(error.contains("locked out"));
    }

    @AfterTest
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
