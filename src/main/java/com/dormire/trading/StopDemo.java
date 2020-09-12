package com.dormire.trading;

import com.dormire.trading.utils.DriverHandler;
import com.dormire.trading.utils.ProfitChecker;
import com.dormire.trading.utils.Util;
import org.openqa.selenium.WebDriver;

import java.util.Scanner;

public class StopDemo {

    /**
     * Waiting time (in seconds).
     */
    private static final int WAIT_TIME = 5;

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = DriverHandler.getDriver();

        Scanner sc = new Scanner(System.in);

        String ticker;
        do {
            Util.showAlert("Please enter the stonk ticker");
            showPromptSymbol();
            ticker = sc.nextLine();
        }
        while (!isAlpha(ticker));

        String url = String.format("https://www.tradingview.com/chart/?symbol=%s", ticker);
        driver.get(url);

        // Step 1
        Util.showAlert("Please enter the buy price:");
        showPromptSymbol();
        double transactionPrice = Double.parseDouble(sc.nextLine());

        Util.showAlert("Please enter the number of stonks:");
        showPromptSymbol();
        int noStonks = Integer.parseInt(sc.nextLine());

        Util.showAlert("Please enter the wanted profit percentage:");
        showPromptSymbol();
        double profitPercentage = Double.parseDouble(sc.nextLine());

        ProfitChecker checker = new ProfitChecker(driver, transactionPrice, profitPercentage);
        checker.start();

        while (true) {
            // Step 2
            Thread.sleep(WAIT_TIME * 1000);

            while (!(Util.getCurrentPrice(driver) > transactionPrice)) {
                Thread.sleep(1000);
            }

            String message = String.format("Please set stop loss at $%f for %d stonks\n", transactionPrice, noStonks);
            Util.showAlert(message);

            // Step 3
            do {
                Util.showAlert("Please type 'OK' to confirm setting stop loss:");
                showPromptSymbol();
            }
            while (!sc.nextLine().equals("OK"));

            // Step 4
            while (!(Util.getCurrentPrice(driver) < transactionPrice)) {
                Thread.sleep(1000);
            }

            Util.showAlert("Stop loss has been activated");

            Util.showAlert("Please enter sell fill price:");
            showPromptSymbol();
            transactionPrice = Double.parseDouble(sc.nextLine());

            Thread.sleep(WAIT_TIME * 1000);

            // Step 5
            while (!(Util.getCurrentPrice(driver) < transactionPrice)) {
                Thread.sleep(1000);
            }

            message = String.format("Please set stop buy at $%f for %d stonks\n", transactionPrice, noStonks);
            Util.showAlert(message);

            // Step 6
            do {
                Util.showAlert("Please type 'OK' to confirm setting stop buy:");
                showPromptSymbol();
            }
            while (!sc.nextLine().equals("OK"));

            // Step 7
            while (!(Util.getCurrentPrice(driver) > transactionPrice)) {
                Thread.sleep(1000);
            }

            Util.showAlert("Stop buy has been activated");

            Util.showAlert("Please enter buy fill price:");
            showPromptSymbol();
            transactionPrice = Double.parseDouble(sc.nextLine());
        }
    }

    private static void showPromptSymbol() {
        System.out.print("> ");
    }

    public static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }
}
