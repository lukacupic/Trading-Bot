package com.dormire.trading;

import com.dormire.trading.utils.DriverHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class StonkDemo {

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = DriverHandler.getDriver();

        driver.get("https://demo.trading212.com/");

        driver.findElement(By.id("username-real")).sendKeys("lukacupic42@gmail.com");
        driver.findElement(By.id("pass-real")).sendKeys("#&34T%8gHdKxYa@Ez7iclJOhFx!U80ou^vZ");
        driver.findElement(By.className("button-login")).submit();

        fetchStonks(driver);

        driver.quit();
    }

    private static void fetchStonks(WebDriver driver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 20);

        List<WebElement> instruments = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("invest-tradebox")));

        while (true) {
            for (WebElement ins : instruments) {
                try {
                    String insName = ins.findElement(By.className("instrument-name")).getText();

                    WebElement insPrice = ins.findElement(By.className("instrument-price"));
                    String intPrice = insPrice.findElement(By.className("integer-value")).getText();
                    String decPrice = insPrice.findElement(By.className("decimal-value")).getText();

                    String insNameOffset = " ".repeat(30 - insName.length());
                    System.out.printf("%s%s \t\t | $%s%s\n", insName, insNameOffset, intPrice, decPrice);
                } catch (RuntimeException ignorable) {
                    // if an error occurs while fetching stock values, simply ignore it
                    System.out.println("err");
                }
            }
            System.out.println();

            Thread.sleep(1000);
        }
    }
}