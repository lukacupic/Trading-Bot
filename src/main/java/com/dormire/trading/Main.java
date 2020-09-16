package com.dormire.trading;

import com.dormire.trading.utils.RuntimeUtil;

import java.util.function.Consumer;

/**
 * Entry class for the 'Stonk' program.
 */
public class Main {

    private static final int WAIT_TIME = 5;
    private static final int LOOP_TIME = 1;

    private static String ticker;
    private static double transactionPrice;
    private static double profitPercentage;
    private static int noStonks;

    private static IOHandler io = new IOHandler();
    private static StonkDriver driver;

    /**
     * Starts the Stonk program.
     *
     * @param args command lines arguments; not used in this program
     */
    public static void main(String[] args) {
        do {
            ticker = io.getString("Enter the stonk ticker");
        }
        while (!isAlphabetical(ticker));

        String url = String.format("https://www.tradingview.com/chart/?symbol=%s", ticker);
        driver = new StonkDriver(url);

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
    private static void step1() {
        transactionPrice = io.getDouble("Please enter the buy price:");
        noStonks = io.getInteger("Please enter the number of stonks:");
        profitPercentage = io.getDouble("Please enter the wanted profit percentage:");

        ProfitChecker checker = new ProfitChecker(driver, transactionPrice, profitPercentage);
        checker.notify(profit -> {
            String message = String.format("Please set stop loss at $%.2f for %d stonks.", profit, noStonks);
            io.showAlert(message);
        });
    }

    /**
     * Step 2 of the algorithm.
     */
    private static void step2() {
        RuntimeUtil.wait(WAIT_TIME);
        RuntimeUtil.waitConditional(() -> driver.getCurrentPrice() > transactionPrice, LOOP_TIME);

        String message = String.format("Please set stop loss at $%.2f for %d stonks\n", transactionPrice, noStonks);
        io.showAlert(message);
    }

    /**
     * Step 3 of the algorithm.
     */
    private static void step3() {
        do {
            io.showInputAlert("Please type 'OK' to confirm setting stop loss:");
        }
        while (!io.isInput("OK"));
    }

    /**
     * Step 4 of the algorithm.
     */
    private static void step4() {
        RuntimeUtil.waitConditional(() -> driver.getCurrentPrice() < transactionPrice, LOOP_TIME);

        io.showAlert("Stop loss has been activated");
        transactionPrice = io.getDouble("Please enter sell fill price:");

        RuntimeUtil.wait(WAIT_TIME);
    }

    /**
     * Step 5 of the algorithm.
     */
    private static void step5() {
        RuntimeUtil.waitConditional(() -> driver.getCurrentPrice() < transactionPrice, LOOP_TIME);

        String message = String.format("Please set stop buy at $%.2f for %d stonks\n", transactionPrice, noStonks);
        io.showAlert(message);
    }

    /**
     * Step 6 of the algorithm.
     */
    private static void step6() {
        do {
            io.showInputAlert("Please type 'OK' to confirm setting stop buy:");
        }
        while (!io.isInput("OK"));
    }

    /**
     * Step 7 of the algorithm.
     */
    private static void step7() {
        RuntimeUtil.waitConditional(() -> driver.getCurrentPrice() > transactionPrice, LOOP_TIME);

        io.showAlert("Stop buy has been activated");
        transactionPrice = io.getDouble("Please enter buy fill price:");
    }

    private static boolean isAlphabetical(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }
}
