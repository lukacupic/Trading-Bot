package com.dormire.trading;

import java.util.function.Consumer;

public class ProfitChecker extends Thread {

    private StonkDriver driver;
    private double transactionPrice;
    private double profitPercentage;

    private Consumer<Double> listener;

    public ProfitChecker(StonkDriver driver, double transactionPrice, double profitPercentage) {
        this.driver = driver;
        this.transactionPrice = transactionPrice;
        this.profitPercentage = profitPercentage;
    }

    public void notify(Consumer<Double> listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        double profit = (1 + this.profitPercentage / 100) * transactionPrice;
        try {
            while (!(this.driver.getCurrentPrice() >= profit)) {
                Thread.sleep(1000);
            }
        } catch (RuntimeException | InterruptedException ignorable) {
        }

        if (listener == null) {
            throw new IllegalStateException("No listener for ProfitChecker!");
        }

        listener.accept(profit);
    }
}
