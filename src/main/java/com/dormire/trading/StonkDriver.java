package com.dormire.trading;

import com.dormire.trading.utils.OSValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * StonkDriver class is a thin wrapper around Selenium's WebDriver.
 * <p>
 * It is initialized by passing the URL of an arbitrary stonk.
 * Driver's methods can then be used for collecting stonk information,
 * such as current price (stonk value).
 */
public class StonkDriver {

    /**
     * The internal WebDriver.
     */
    private WebDriver driver;

    /**
     * Creates a new stonk driver.
     */
    public StonkDriver(String url) {
        this.driver = getDriver();
        this.driver.get(url);
    }

    /**
     * Initializes and returns a new driver depending on the running OS.
     *
     * @return a WebDriver instance
     */
    public static WebDriver getDriver() {
        if (OSValidator.isWindows()) {
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium_Drivers\\chromedriver.exe");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            return new ChromeDriver(options);

        } else if (OSValidator.isLinux()) {
            System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");

            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            return new FirefoxDriver(options);

        } else {
            throw new IllegalStateException("Unknown Operating System -- no available driver");
        }
    }

    /**
     * Returns the current price of the stonk.
     *
     * @return current stonk price
     */
    public double getCurrentPrice() {
        WebElement element = this.driver.findElement(By.className("buttonText-1vopxN9j"));
        return Double.parseDouble(element.getText());
    }
}
