package com.dormire.trading.algorithm;

import com.dormire.trading.algorithm.driver.StonkDriver;
import com.dormire.trading.algorithm.driver.StonkDriverManager;
import com.dormire.trading.algorithm.utils.PriceType;
import com.dormire.trading.algorithm.utils.RuntimeUtil;
import com.dormire.trading.algorithm.utils.TimeUtil;
import com.dormire.trading.gui.instruments.Instrument;
import com.dormire.trading.gui.GuiManager;
import javafx.application.Platform;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StonkTrader extends Thread {

    private static final int LOOP_TIME = 1; /* in seconds */
    private static final int WAIT_TIME = 5 * 60; /* in seconds */
    private static final double BUFFER_PERCENTAGE = 0.005;

    private StonkDriver driver;
    private GuiManager guiManager;
    private StonkDriverManager driverManager;
    private BlockingQueue<String> queue;

    private String ticker;
    private double transactionPrice;
    private double profitPercentage;
    private double noStonks;

    private int currentStep;
    private String currentMessage;

    public StonkTrader(GuiManager guiManager, StonkDriverManager driverManager, Instrument instrument) {
        this.guiManager = guiManager;
        this.driverManager = driverManager;
        this.queue = new ArrayBlockingQueue<>(1);
        this.setDaemon(true);

        this.ticker = instrument.getTicker();
        this.transactionPrice = instrument.getPrice();
        this.profitPercentage = instrument.getPercentage();
        this.noStonks = instrument.getNoStonks();
    }

    @Override
    public void run() {
        try {
            init();

            while (true) {
                step1();
                step2();
                step3();
                step4();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() throws InterruptedException {
        setMessage("Loading, please wait...");

        String url = String.format("https://www.tradingview.com/chart/?symbol=%s", ticker);
        driver = driverManager.get();
        driver.load(url);

        ProfitChecker checker = new ProfitChecker(driver, transactionPrice, profitPercentage);
        checker.notify(profit -> showOkAlert("Your profit goal has been reached!\n" +
                "Set stop loss for %s at $%.2f for %f stonks.", ticker, profit, noStonks));
        checker.start();
    }

    private void step1() {
        setStep(1);

        CountdownTimer timer = new CountdownTimer(WAIT_TIME, seconds -> {
            String format1 = "Waiting for price > %.2f ";
            String format2 = "&& timer > " + TimeUtil.formatTime(seconds);

            if (seconds > 0) {
                setMessage(format1 + format2 + "...", transactionPrice);
            } else {
                setMessage(format1 + "...", transactionPrice);
            }
        });
        timer.start();

        while (!(driver.getCurrentPrice(PriceType.SELL) > transactionPrice)) {
            RuntimeUtil.sleep(1);
        }

        showNotification("Set stop loss for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);
        showOkAlert("Set stop loss for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);
    }

    private void step2() throws InterruptedException {
        setStep(2);
        setMessage("Waiting for price <= %.2f...", transactionPrice);

        while (!(driver.getCurrentPrice(PriceType.SELL) <= (1 + BUFFER_PERCENTAGE) * transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        showNotification("Has the stop loss for %s been activated?", ticker);
        String response = showYesNoAlert("Has the stop loss for %s been activated?", ticker);

        if (response.equals("YES")) {
            String fillPrice = showInputDialog("Please enter sell fill price for %s.", ticker);
            transactionPrice = Double.parseDouble(fillPrice);

            RuntimeUtil.sleep(WAIT_TIME);

        } else if (response.equals("NO")) {
            RuntimeUtil.sleep(60);
            step2();
        }
    }

    private void step3() {
        setStep(3);

        CountdownTimer timer = new CountdownTimer(WAIT_TIME, seconds -> {
            String format1 = "Waiting for price < %.2f ";
            String format2 = "&& timer > " + TimeUtil.formatTime(seconds);

            if (seconds > 0) {
                setMessage(format1 + format2 + "...", transactionPrice);
            } else {
                setMessage(format1 + "...", transactionPrice);
            }
        });
        timer.start();

        while (!(driver.getCurrentPrice(PriceType.BUY) < transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        showNotification("Set stop buy for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);
        showOkAlert("Set stop buy for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);
    }

    private void step4() throws InterruptedException {
        setStep(4);
        setMessage("Waiting for price >= %.2f...", transactionPrice);

        while (!(driver.getCurrentPrice(PriceType.BUY) >= (1 - BUFFER_PERCENTAGE) * transactionPrice)) {
            RuntimeUtil.sleep(LOOP_TIME);
        }

        showNotification("Has the stop buy for %s been activated?", ticker);
        String response = showYesNoAlert("Has the stop buy for %s been activated?", ticker);

        if (response.equals("YES")) {
            String fillPrice = showInputDialog("Please enter buy fill price for %s.", ticker);
            transactionPrice = Double.parseDouble(fillPrice);

        } else if (response.equals("NO")) {
            RuntimeUtil.sleep(60);
            step4();
        }
    }

    // Internal GUI-handling methods (temporary, I hope...)

    private void setMessage(String format, Object... arguments) {
        this.currentMessage = String.format(format, arguments);
        if (guiManager.getActiveTrader() != this) return;
        Platform.runLater(() -> guiManager.setMessage(format, arguments));
    }

    private void setStep(int step) {
        this.currentStep = step;
        if (guiManager.getActiveTrader() != this) return;
        Platform.runLater(() -> guiManager.setStep(step));
    }

    private void showOkAlert(String format, Object... arguments) {
        Platform.runLater(() -> {
            try {
                guiManager.showOkAlert(format, arguments);
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        try {
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String format, Object... arguments) {
        Platform.runLater(() -> guiManager.showNotification(format, arguments));
    }

    private String showYesNoAlert(String format, Object... arguments) throws InterruptedException {
        Platform.runLater(() -> {
            try {
                String response = guiManager.showYesNoAlert(format, arguments);
                queue.put(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return queue.take();
    }

    private String showInputDialog(String format, Object... arguments) throws InterruptedException {
        Platform.runLater(() -> {
            try {
                String fillPrice = guiManager.showInputDialog(format, arguments);
                queue.put(fillPrice);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return queue.take();
    }

    public int getStep() {
        return currentStep;
    }

    public String getMessage() {
        return currentMessage;
    }

}