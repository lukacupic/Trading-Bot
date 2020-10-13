package com.dormire.trading.algorithm.driver;

import com.dormire.trading.algorithm.utils.OSValidator;
import com.dormire.trading.algorithm.utils.PriceType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.List;

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
    public StonkDriver() {
        this.driver = getDriver();
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
     * Loads the given URL.
     *
     * @param url the url to load
     */
    public void load(String url) {
        this.driver.get(url);
    }

    /**
     * Returns the current price of the stonk.
     *
     * @param type the price type to fetch (BUY or SELL)
     * @return current stonk price
     */
    public double getCurrentPrice(PriceType type) {
        List<WebElement> elements = this.driver.findElements(By.className("buttonText-1vopxN9j"));
        return Double.parseDouble(elements.get(type.equals(PriceType.SELL) ? 0 : 1).getText());
    }
}
