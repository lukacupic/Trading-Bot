package com.dormire.trading.algorithm;

import com.dormire.trading.algorithm.driver.StonkDriver;
import com.dormire.trading.algorithm.driver.StonkDriverManager;
import com.dormire.trading.algorithm.utils.PriceType;
import com.dormire.trading.algorithm.utils.RuntimeUtil;
import com.dormire.trading.gui.RingManager;
import javafx.application.Platform;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StonkTrader {

    private static final int LOOP_TIME = 1; /* in seconds */
    private static final int WAIT_TIME = 5; /* in seconds */
    private static final double BUFFER_PERCENTAGE = 0.5;

    private StonkDriver driver;
    private RingManager ringManager;
    private StonkDriverManager driverManager;
    private BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

    private String ticker;
    private double transactionPrice;
    private double profitPercentage;
    private double noStonks;

    public StonkTrader(RingManager ringManager, StonkDriverManager driverManager,
                       String ticker, double transactionPrice, double profitPercentage, double noStonks) {
        this.ringManager = ringManager;
        this.driverManager = driverManager;
        this.ticker = ticker;
        this.transactionPrice = transactionPrice;
        this.profitPercentage = profitPercentage;
        this.noStonks = noStonks;
    }

    public void start() throws InterruptedException {
        init();

        while (true) {
            step1();
            step2();
            step3();
            step4();
        }
    }

    private void init() throws InterruptedException {
        String url = String.format("https://www.tradingview.com/chart/?symbol=%s", ticker);
        driver = driverManager.get();
        driver.load(url);

        ProfitChecker checker = new ProfitChecker(driver, transactionPrice, profitPercentage);
        checker.notify(profit -> Platform.runLater(() -> {
            String message = String.format("Your profit goal has been reached!\n" +
                    "Set stop loss at $%.2f for %f stonks.", profit, noStonks);
            ringManager.showOkAlert(message);
        }));
        checker.start();
    }

    private void step1() throws InterruptedException {
        Platform.runLater(() -> {
            ringManager.setCurrentStep(1);
            ringManager.setMessage("Waiting for price > %.2f && timer > 5 min...", transactionPrice);
        });

        RuntimeUtil.sleep(WAIT_TIME);

        while (!(driver.getCurrentPrice(PriceType.SELL) > transactionPrice)) {
            RuntimeUtil.sleep(1);
        }

        ringManager.showNotification("Set stop loss for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);

        Platform.runLater(() -> {
            try {
                ringManager.showOkAlert("Set stop loss at $%.2f for %f stonks.", transactionPrice, noStonks);
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        queue.take();
    }

    private void step2() throws InterruptedException {
        Platform.runLater(() -> {
            ringManager.setCurrentStep(2);
            ringManager.setMessage("Waiting for price <= %.2f...", transactionPrice);
        });

        while (!(driver.getCurrentPrice(PriceType.SELL) <= BUFFER_PERCENTAGE * transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        ringManager.showNotification("Has the stop loss been activated?");

        Platform.runLater(() -> {
            try {
                String response = ringManager.showYesNoAlert("Has the stop loss been activated?");
                queue.put(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        String response = queue.take();

        if (response.equals("YES")) {
            Platform.runLater(() -> {
                try {
                    String fillPrice = ringManager.showInputDialog("Please enter sell fill price.");
                    queue.put(fillPrice);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            transactionPrice = Double.parseDouble(queue.take());
            RuntimeUtil.sleep(WAIT_TIME);

        } else if (response.equals("NO")) {
            RuntimeUtil.sleep(60);
            step2();
        }
    }

    private void step3() throws InterruptedException {
        Platform.runLater(() -> {
            ringManager.setCurrentStep(3);
            ringManager.setMessage("Waiting for price < %.2f && timer > 5 min...", transactionPrice);
        });

        RuntimeUtil.sleep(WAIT_TIME);

        while (!(driver.getCurrentPrice(PriceType.BUY) < transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        ringManager.showNotification("Set stop buy for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);

        Platform.runLater(() -> {
            try {
                ringManager.showOkAlert("Set stop buy at $%.2f for %f stonks.", transactionPrice, noStonks);
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        queue.take();
    }

    private void step4() throws InterruptedException {
        Platform.runLater(() -> {
            ringManager.setCurrentStep(4);
            ringManager.setMessage("Waiting for price >= %.2f...", transactionPrice);
        });

        while (!(driver.getCurrentPrice(PriceType.BUY) >= (2 - BUFFER_PERCENTAGE) * transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        ringManager.showNotification("Has the stop buy been activated?");

        Platform.runLater(() -> {
            try {
                String response = ringManager.showYesNoAlert("Has the stop buy been activated?");
                queue.put(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        String response = queue.take();


        if (response.equals("YES")) {
            Platform.runLater(() -> {
                try {
                    String fillPrice = ringManager.showInputDialog("Please enter buy fill price.");
                    queue.put(fillPrice);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            transactionPrice = Double.parseDouble(queue.take());

        } else if (response.equals("NO")) {
            RuntimeUtil.sleep(60);
            step4();
        }
    }
}