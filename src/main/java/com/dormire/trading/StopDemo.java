package com.dormire.trading;

import com.dormire.trading.util.Initializer;
import dorkbox.notify.Notify;
import dorkbox.notify.Pos;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class StopDemo {

    /**
     * Waiting time (in seconds).
     */
    private static int WAIT_TIME = 5*60;

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = Initializer.getDriver();

        Scanner sc = new Scanner(System.in);

        String ticker;
        do {
            showAlert("Please enter the stonk ticker");
            showPromptSymbol();
            ticker = sc.nextLine();
        }
        while (!isAlpha(ticker));

        String url = String.format("https://www.tradingview.com/symbols/%s/", ticker);
        driver.get(url);

        // Step 1
        showAlert("Please enter the buy price:");

        showPromptSymbol();
        double buyPrice = Double.parseDouble(sc.nextLine());

        while (!(getCurrentPrice(driver) >= 1.012 * buyPrice)) {
            // Keep checking for a profit goal
            Thread.sleep(1000);
        }

        String message = String.format("Please raise stop loss to $%f\n", 1.012 * buyPrice);
        showAlert(message);

        while (true) {
            // Step 2
            Thread.sleep(WAIT_TIME * 1000);

            while (!(getCurrentPrice(driver) > buyPrice)) {
                Thread.sleep(1000);
            }

            message = String.format("Please set stop loss at $%f\n", buyPrice);
            showAlert(message);

            // Step 3
            do {
                showAlert("Please type 'OK' to confirm setting stop loss:");
                showPromptSymbol();
            }
            while (!sc.nextLine().equals("OK"));

            // Step 4
            while (!(getCurrentPrice(driver) < buyPrice)) {
                Thread.sleep(1000);
            }

            showAlert("Stop loss has been activated");

            Thread.sleep(WAIT_TIME * 1000);

            // Step 5
            while (!(getCurrentPrice(driver) < buyPrice)) {
                Thread.sleep(1000);
            }

            message = String.format("Please set stop buy at $%f\n", buyPrice);
            showAlert(message);

            // Step 6
            do {
                showAlert("Please type 'OK' to confirm setting stop buy:");
                showPromptSymbol();
            }
            while (!sc.nextLine().equals("OK"));

            // Step 7
            while (!(getCurrentPrice(driver) > buyPrice)) {
                Thread.sleep(1000);
            }

            showAlert("Stop buy has been activated");
        }
    }

    private static void showPromptSymbol() {
        System.out.print("> ");
    }

    private static void showAlert(String message) {
        new Thread(() -> {
            try {
                playSound();

                Notify.create()
                        .title("Papi Musk")
                        .position(Pos.TOP_RIGHT)
                        .text(message)
                        .setScreen(1)
                        .image(ImageIO.read(new File("src/main/resources/musk.png")))
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void playSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File f = new File("src/main/resources/ding.wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.loop(1);
    }

    private static double getCurrentPrice(WebDriver driver) {
        WebElement element = driver.findElement(By.xpath("/html/body/div[2]/div[5]/div/header/div/div[3]/div[1]/div/div/div/div[1]/div[1]"));
        return Double.parseDouble(element.getText());
    }

    public static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }
}
