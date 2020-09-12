package com.dormire.trading;

public class ProfitChecker extends Thread {

    private StonkDriver driver;
    private IOHandler io;
    private double transaction;
    private double profitPercentage;

    public ProfitChecker(StonkDriver driver, IOHandler io, double transaction, double profitPercentage) {
        this.driver = driver;
        this.io = io;
        this.transaction = transaction;
        this.profitPercentage = profitPercentage;
    }

    @Override
    public void run() {
        try {
            while (!(this.driver.getCurrentPrice() >= (1 + this.profitPercentage / 100) * transaction)) {
                Thread.sleep(1000);
            }
        } catch (RuntimeException | InterruptedException ignorable) {
        }

        io.showAlert("Please set stop loss at $" + (1 + this.profitPercentage / 100) * transaction);
    }
}
