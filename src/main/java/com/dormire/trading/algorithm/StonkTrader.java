package com.dormire.trading.algorithm;

import com.dormire.trading.algorithm.driver.StonkDriver;
import com.dormire.trading.algorithm.driver.StonkDriverManager;
import com.dormire.trading.algorithm.utils.PriceType;
import com.dormire.trading.algorithm.utils.TimeUtil;
import com.dormire.trading.gui.instruments.Instrument;
import com.dormire.trading.gui.GUIManager;
import javafx.application.Platform;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StonkTrader extends Thread {

    private StonkDriver driver;
    private GUIManager guiManager;
    private StonkDriverManager driverManager;
    private ProfitChecker profitChecker;
    private BlockingQueue<Object> queue;
    private CountdownTimer timer;

    private String ticker;
    private double transactionPrice;
    private double profit;
    private double noStonks;

    private int loopTime;
    private int waitTime;

    private double bufferZone;
    private double visualBuffer = 0.002;

    private int currentStep;
    private String currentMessage;

    public StonkTrader(GUIManager guiManager, StonkDriverManager driverManager, Instrument instrument) {
        this.guiManager = guiManager;
        this.driverManager = driverManager;
        this.queue = new ArrayBlockingQueue<>(1);
        this.timer = new CountdownTimer();
        this.setDaemon(true);

        this.ticker = instrument.getTicker();
        this.transactionPrice = instrument.getPrice();
        this.profit = instrument.getProfit();
        this.noStonks = instrument.getNoStonks();
        this.loopTime = instrument.getLoopTime();
        this.waitTime = instrument.getWaitTime();
        this.bufferZone = instrument.getBufferZone();
    }

    @Override
    public void run() {
        try {
            init();

            while (!isInterrupted()) {
                step1();
                step2();
                step3();
                step4();
            }
        } catch (InterruptedException ignored) {
        }

        shutdown();
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    private void init() throws InterruptedException {
        setMessage("Loading, please wait...");

        String url = String.format("https://www.tradingview.com/chart/?symbol=%s", ticker);
        driver = driverManager.get();
        driver.load(url);

        profitChecker = new ProfitChecker(driver, transactionPrice, profit);
        profitChecker.notify(profit -> showOkAlert("Your profit goal has been reached!\n" +
                "Set stop loss for %s at $%.2f for %f stonks.", ticker, profit, noStonks));
        profitChecker.start();
    }

    private void step1() throws InterruptedException {
        setStep(1);

        timer.setTask(seconds -> {
            String format1 = "Waiting for price > %.2f ";
            String format2 = "&& timer > " + TimeUtil.formatTime(seconds);

            if (seconds > 0) {
                setMessage(format1 + format2 + "...", transactionPrice);
            } else {
                setMessage(format1 + "...", transactionPrice);
            }
        });
        timer.setTime(waitTime);
        timer.start();

        while (!(driver.getCurrentPrice(PriceType.SELL) > transactionPrice)) {
            sleep(1);
        }

        showNotification("Set stop loss for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);
        showOkAlert("Set stop loss for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);
    }

    private void step2() throws InterruptedException {
        setStep(2);
        setMessage("Waiting for price <= %.2f...", transactionPrice);

        while (!(driver.getCurrentPrice(PriceType.SELL) <= (1 + bufferZone) * transactionPrice)) {
            sleep(loopTime);
        }

        showNotification("Has the stop loss for %s been activated?", ticker);
        String response = showYesNoAlert("Has the stop loss for %s been activated?", ticker);

        if (response.equals("YES")) {
            transactionPrice = showInputDialog("Please enter sell fill price for %s.", ticker);

            sleep(waitTime);

        } else if (response.equals("NO")) {
            sleep(60);
            step2();
        }
    }

    private void step3() throws InterruptedException {
        setStep(3);

        timer.setTask(seconds -> {
            String format1 = "Waiting for price < %.2f ";
            String format2 = "&& timer > " + TimeUtil.formatTime(seconds);

            if (seconds > 0) {
                setMessage(format1 + format2 + "...", transactionPrice);
            } else {
                setMessage(format1 + "...", transactionPrice);
            }
        });
        timer.setTime(waitTime);
        timer.start();

        while (!(driver.getCurrentPrice(PriceType.BUY) < transactionPrice)) {
            sleep(loopTime);
        }

        showNotification("Set stop buy for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);
        showOkAlert("Set stop buy for %s at $%.2f for %f stonks.", ticker, transactionPrice, noStonks);
    }

    private void step4() throws InterruptedException {
        setStep(4);
        setMessage("Waiting for price >= %.2f...", transactionPrice);

        while (!(driver.getCurrentPrice(PriceType.BUY) >= (1 - bufferZone) * transactionPrice)) {
            sleep(loopTime);
        }

        showNotification("Has the stop buy for %s been activated?", ticker);
        String response = showYesNoAlert("Has the stop buy for %s been activated?", ticker);

        if (response.equals("YES")) {
            transactionPrice = showInputDialog("Please enter buy fill price for %s.", ticker);

        } else if (response.equals("NO")) {
            sleep(60);
            step4();
        }
    }

    static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    // Internal GUI-handling methods (temporary, I hope...)

    private void setMessage(String format, Object... arguments) {
        this.currentMessage = String.format(format, arguments);
        if (guiManager.getActiveTrader() != this) return;
        Platform.runLater(() -> guiManager.updateMessage(format, arguments));
    }

    private void setStep(int step) {
        this.currentStep = step;
        if (guiManager.getActiveTrader() != this) return;
        Platform.runLater(() -> guiManager.updateStep(step));
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
        return (String) queue.take();
    }

    private double showInputDialog(String format, Object... arguments) throws InterruptedException {
        Platform.runLater(() -> {
            try {
                double fillPrice = guiManager.showNumberInputDialog(format, arguments);
                queue.put(fillPrice);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return (Double) queue.take();
    }

    public int getStep() {
        return currentStep;
    }

    public String getMessage() {
        return currentMessage;
    }

    public void shutdown() {
        setStep(0);
        setMessage("");
        if (profitChecker != null) profitChecker.interrupt();
        if (driver != null) driver.shutdown();
    }

    public void update(Instrument instrument) {
        transactionPrice = instrument.getPrice();
        noStonks = instrument.getNoStonks();
        profit = instrument.getProfit();
        loopTime = instrument.getLoopTime();
        waitTime = instrument.getWaitTime();
        timer.setTime(waitTime);
    }
}