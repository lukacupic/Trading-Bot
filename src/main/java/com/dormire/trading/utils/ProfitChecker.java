package com.dormire.trading.utils;

import org.openqa.selenium.WebDriver;

public class ProfitChecker extends Thread {

    private WebDriver driver;
    private double buyPrice;
    private double profitPercentage;

    public ProfitChecker(WebDriver driver, double buyPrice, double profitPercentage) {
        this.driver = driver;
        this.buyPrice = buyPrice;
        this.profitPercentage = profitPercentage;
    }

    @Override
    public void run() {
        while (!(Util.getCurrentPrice(driver) >= (1 + this.profitPercentage / 100) * buyPrice)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignorable) {
            }
        }

        Util.showAlert("Please set stop loss at $" + (1 + this.profitPercentage / 100) * buyPrice);
    }
}
