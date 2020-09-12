package com.dormire.trading.utils;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static double getCurrentPrice(WebDriver driver) {
//        WebElement element = driver.findElement(By.xpath("/html/body/div[2]/div[5]/div/header/div/div[3]/div[1]/div/div/div/div[1]/div[1]"));
        WebElement element = driver.findElement(By.className("buttonText-1vopxN9j"));
        return Double.parseDouble(element.getText());
    }

    public static void showAlert(String message) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String timedMessage = String.format("[%s] %s", currentTime, message);

        System.out.println(timedMessage);

        new Thread(() -> {
            try {
                playSound();

                Notify.create()
                        .title("Papi Musk")
                        .position(Pos.TOP_LEFT)
                        .text(timedMessage)
                        .setScreen(1)
                        .image(ImageIO.read(new File("src/main/resources/musk.png")))
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void playSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File f = new File("src/main/resources/ding.wav");
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }
}
