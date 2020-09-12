package com.dormire.trading.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverHandler {

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
            return new FirefoxDriver();

        } else {
            throw new IllegalStateException("Unknown Operating System -- no available driver");
        }
    }
}
