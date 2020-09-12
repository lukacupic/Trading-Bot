package com.dormire.trading.utils;

import org.openqa.selenium.WebDriver;

public class ProfitChecker extends Thread {

    private WebDriver driver;
    private double transaction;
    private double profitPercentage;

    public ProfitChecker(WebDriver driver, double transaction, double profitPercentage) {
        this.driver = driver;
        this.transaction = transaction;
        this.profitPercentage = profitPercentage;
    }

    @Override
    public void run() {
        try {
            while (!(Util.getCurrentPrice(driver) >= (1 + this.profitPercentage / 100) * transaction)) {
                Thread.sleep(1000);
            }
        } catch (RuntimeException | InterruptedException ignorable) {
        }

        Util.showAlert("Please set stop loss at $" + (1 + this.profitPercentage / 100) * transaction);
    }
}
