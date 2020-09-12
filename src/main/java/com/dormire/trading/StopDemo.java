package com.dormire.trading;

public class StopDemo {

    private static final int WAIT_TIME = 5;

    private static String ticker;
    private static double transactionPrice;
    private static double profitPercentage;
    private static int noStonks;

    private static IOHandler io = new IOHandler();
    private static StonkDriver driver;
    private static ProfitChecker checker;

    public static void main(String[] args) throws InterruptedException {
        do {
            ticker = io.getString("Please enter the stonk ticker");
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

    private static void step1() {
        transactionPrice = io.getDouble("Please enter the buy price:");
        noStonks = io.getInteger("Please enter the number of stonks:");
        profitPercentage = io.getDouble("Please enter the wanted profit percentage:");

        checker = new ProfitChecker(driver, io, transactionPrice, profitPercentage);
        checker.start();
    }

    private static void step2() throws InterruptedException {
        Thread.sleep(WAIT_TIME * 1000);

        while (!(driver.getCurrentPrice() > transactionPrice)) {
            Thread.sleep(1000);
        }

        String message = String.format("Please set stop loss at $%f for %d stonks\n", transactionPrice, noStonks);
        io.showAlert(message);
    }

    private static void step3() {
        do {
            io.showInputAlert("Please type 'OK' to confirm setting stop loss:");
        }
        while (!io.isInput("OK"));
    }

    private static void step4() throws InterruptedException {
        while (!(driver.getCurrentPrice() < transactionPrice)) {
            Thread.sleep(1000);
        }

        io.showAlert("Stop loss has been activated");
        transactionPrice = io.getDouble("Please enter sell fill price:");

        Thread.sleep(WAIT_TIME * 1000);
    }

    private static void step5() throws InterruptedException {
        while (!(driver.getCurrentPrice() < transactionPrice)) {
            Thread.sleep(1000);
        }

        String message = String.format("Please set stop buy at $%f for %d stonks\n", transactionPrice, noStonks);
        io.showAlert(message);
    }

    private static void step6() {
        do {
            io.showInputAlert("Please type 'OK' to confirm setting stop buy:");
        }
        while (!io.isInput("OK"));
    }

    private static void step7() throws InterruptedException {
        while (!(driver.getCurrentPrice() > transactionPrice)) {
            Thread.sleep(1000);
        }

        io.showAlert("Stop buy has been activated");
        transactionPrice = io.getDouble("Please enter buy fill price:");
    }

    private static boolean isAlphabetical(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }
}
