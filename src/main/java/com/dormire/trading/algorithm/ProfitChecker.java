package com.dormire.trading.algorithm;

import com.dormire.trading.algorithm.driver.StonkDriver;
import com.dormire.trading.algorithm.utils.PriceType;

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
        this.setDaemon(true);
    }

    public void notify(Consumer<Double> listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            double profit = (1 + profitPercentage / 100) * transactionPrice;

            while (!(driver.getCurrentPrice(PriceType.BUY) >= profit)) {
                Thread.sleep(1000);
            }

            if (listener == null) {
                throw new IllegalStateException("No listener for ProfitChecker!");
            }

            listener.accept(profit);
        } catch (InterruptedException ignored) {
            //shutdown
        }
    }
}
