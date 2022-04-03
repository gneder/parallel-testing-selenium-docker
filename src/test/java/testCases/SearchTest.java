package testCases;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


public class SearchTest {

    private static WebDriver driver;

    @BeforeTest
    @Parameters("browser")
    void setup(String br) throws MalformedURLException
    {
        String nodeURL = "http://localhost:4444/wd/hub";

        if(br.equals("chrome"))
        {
            ChromeOptions options = new ChromeOptions();
            driver = new RemoteWebDriver(new URL(nodeURL), options);
        }

        else if (br.equals("firefox"))
        {
            FirefoxOptions options = new FirefoxOptions();
            driver = new RemoteWebDriver(new URL(nodeURL), options);
        }

        else if (br.equals("edge"))
        {
            EdgeOptions options = new EdgeOptions();
            driver = new RemoteWebDriver(new URL(nodeURL), options);
        }


        driver.get("https://www.google.com");
        driver.manage().window().maximize();
        Thread.sleep(500);
    }

    @Test
    void searchTestCase()
    {
        driver.findElement(By.xpath("//*[@name='q']")).sendKeys("neder medium");
        driver.findElement(By.xpath("//*[@name='q']")).sendKeys(Keys.ENTER);
        Thread.sleep(500);
        driver.findElement(By.xpath("//h3[contains(text(),'neder - Medium')]")).click();

        String aboutMe = driver.getCurrentUrl();
        System.out.println(aboutMe);
        Assert.assertEquals("https://medium.com/@_gabrielneder", aboutMe);
    }

    @AfterTest
    void teardown()
    {
        driver.quit();
    }
}
