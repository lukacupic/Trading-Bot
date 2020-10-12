package com.dormire.trading.algorithm;

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
    private RingManager manager;
    private BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

    private String ticker;
    private double transactionPrice;
    private double profitPercentage;
    private double noStonks;

    public StonkTrader(RingManager manager, String ticker, double transactionPrice, double profitPercentage, double noStonks) {
        this.manager = manager;
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

    private void init() {
        String url = String.format("https://www.tradingview.com/chart/?symbol=%s", ticker);
        driver = new StonkDriver(url);

        ProfitChecker checker = new ProfitChecker(driver, transactionPrice, profitPercentage);
        checker.notify(profit -> {
            Platform.runLater(() -> {
                String message = String.format("Your profit goal has been reached!\n" +
                        "Set stop loss at $%.2f for %f stonks.", profit, noStonks);
                manager.showOkAlert(message);
            });
        });
        checker.start();
    }

    private void step1() throws InterruptedException {
        Platform.runLater(() -> {
            manager.setCurrentStep(1);
            manager.setMessage("Waiting for price > %.2f && timer > 5 min...", transactionPrice);
        });

        RuntimeUtil.sleep(WAIT_TIME);

        while (!(driver.getCurrentPrice(PriceType.SELL) > transactionPrice)) {
            RuntimeUtil.sleep(1);
        }

        manager.showNotification("Set stop loss for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);

        Platform.runLater(() -> {
            try {
                manager.showOkAlert("Set stop loss at $%.2f for %f stonks.", transactionPrice, noStonks);
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        queue.take();
    }

    private void step2() throws InterruptedException {
        Platform.runLater(() -> {
            manager.setCurrentStep(2);
            manager.setMessage("Waiting for price <= %.2f...", transactionPrice);
        });

        while (!(driver.getCurrentPrice(PriceType.SELL) <= BUFFER_PERCENTAGE * transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        manager.showNotification("Has the stop loss been activated?");

        Platform.runLater(() -> {
            try {
                String response = manager.showYesNoAlert("Has the stop loss been activated?");
                queue.put(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        String response = queue.take();

        if (response.equals("YES")) {
            Platform.runLater(() -> {
                try {
                    String fillPrice = manager.showInputDialog("Please enter sell fill price.");
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
            manager.setCurrentStep(3);
            manager.setMessage("Waiting for price < %.2f && timer > 5 min...", transactionPrice);
        });

        RuntimeUtil.sleep(WAIT_TIME);

        while (!(driver.getCurrentPrice(PriceType.BUY) < transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        manager.showNotification("Set stop buy for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);

        Platform.runLater(() -> {
            try {
                manager.showOkAlert("Set stop buy at $%.2f for %f stonks.", transactionPrice, noStonks);
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        queue.take();
    }

    private void step4() throws InterruptedException {
        Platform.runLater(() -> {
            manager.setCurrentStep(4);
            manager.setMessage("Waiting for price >= %.2f...", transactionPrice);
        });

        while (!(driver.getCurrentPrice(PriceType.BUY) >= (2 - BUFFER_PERCENTAGE) * transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        manager.showNotification("Has the stop buy been activated?");

        Platform.runLater(() -> {
            try {
                String response = manager.showYesNoAlert("Has the stop buy been activated?");
                queue.put(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        String response = queue.take();


        if (response.equals("YES")) {
            Platform.runLater(() -> {
                try {
                    String fillPrice = manager.showInputDialog("Please enter buy fill price.");
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