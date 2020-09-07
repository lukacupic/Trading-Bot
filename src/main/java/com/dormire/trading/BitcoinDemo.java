package com.dormire.trading;

import com.dormire.trading.util.Initializer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BitcoinDemo {

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = Initializer.getDriver();

        driver.get("https://www.tradingview.com/symbols/BTCUSD/");
        driver.manage().deleteAllCookies();

        System.out.println(driver.getTitle());

        double entry_price = 10008;
        double price;

        do {
            String priceString = driver.findElement(By.xpath("/html/body/div[2]/div[5]/div/header/div/div[3]/div[1]/div/div/div/div[1]/div[1]")).getText();
            price = Double.parseDouble(priceString);
            System.out.println(price);

            Thread.sleep(1000);
        } while (price > entry_price);

        System.out.print("StopLoss");
    }
}