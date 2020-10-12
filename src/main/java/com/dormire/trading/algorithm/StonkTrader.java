package com.dormire.trading.algorithm;

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
        step1();

        while (true) {
            step2();
            step3();
            step4();
            step5();
            step6();
            step7();
        }
    }

    /**
     * Step 1 of the algorithm.
     */
    private void step1() {
        String url = String.format("https://www.tradingview.com/chart/?symbol=%s", ticker);
        driver = new StonkDriver(url);
        manager.setCurrentStep(1);

        ProfitChecker checker = new ProfitChecker(driver, transactionPrice, profitPercentage);
        checker.notify(profit -> {
            String message = String.format("Please set stop loss at $%.2f for %f stonks.", profit, noStonks);
            Platform.runLater(() -> {
                manager.showMessage(message);
            });
        });
        checker.start();
    }

    /**
     * Step 2 of the algorithm.
     */
    private void step2() throws InterruptedException {
        RuntimeUtil.sleep(WAIT_TIME);

//        while (!(driver.getCurrentPrice(PriceType.SELL) > transactionPrice)) {
//            RuntimeUtil.sleep(1);
//        }

        String message = String.format("Please set stop loss at $%.2f for %f stonks\n", transactionPrice, noStonks);
        Platform.runLater(() -> {
            try {
                manager.setCurrentStep(2);
                manager.showMessage(message);
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        queue.take();
    }

    /**
     * Step 3 of the algorithm.
     */
    private void step3() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                manager.setCurrentStep(3);
                manager.showOkAlert("Please click 'OK' to confirm setting stop loss");
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        queue.take();
    }

    /**
     * Step 4 of the algorithm.
     */
    private void step4() throws InterruptedException {
//        while (!(driver.getCurrentPrice(PriceType.SELL) < BUFFER_PERCENTAGE * transactionPrice)) {
//            RuntimeUtil.sleep(LOOP_TIME);
//        }

        Platform.runLater(() -> {
            try {
                manager.setCurrentStep(4);
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
                    manager.setCurrentStep(5);
                    String fillPrice = manager.showInputDialog("Please enter sell fill price");
                    queue.put(fillPrice);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            transactionPrice = Double.parseDouble(queue.take());
            RuntimeUtil.sleep(WAIT_TIME);

        } else if (response.equals("NO")) {
            RuntimeUtil.sleep(60);
            step4();
        }
    }

    /**
     * Step 5 of the algorithm.
     */
    private void step5() {
//        while (!(driver.getCurrentPrice(PriceType.BUY) < transactionPrice)) {
//            RuntimeUtil.sleep(LOOP_TIME);
//        }

        manager.setCurrentStep(5);
    }

    /**
     * Step 6 of the algorithm.
     */
    private void step6() throws InterruptedException {
        String message = String.format("Please set stop buy at $%.2f for %f stonks\n" +
                "Click 'OK' to confirm setting stop buy.", transactionPrice, noStonks);

        Platform.runLater(() -> {
            try {
                manager.setCurrentStep(6);
                manager.showOkAlert(message);
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        queue.take();
    }

    /**
     * Step 7 of the algorithm.
     */
    private void step7() throws InterruptedException {
//        while (!(driver.getCurrentPrice(PriceType.BUY) > (2 - BUFFER_PERCENTAGE) * transactionPrice)) {
//            RuntimeUtil.sleep(LOOP_TIME);
//        }

        Platform.runLater(() -> {
            try {
                manager.setCurrentStep(7);
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
                    String fillPrice = manager.showInputDialog("Stop buy has been activated. Please enter buy fill price");
                    queue.put(fillPrice);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            transactionPrice = Double.parseDouble(queue.take());

        } else if (response.equals("NO")) {
            RuntimeUtil.sleep(60);
            step7();
        }
    }
}
