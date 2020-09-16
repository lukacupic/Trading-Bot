package com.dormire.trading.utils;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IOUtil {

    public static void showAlert(String message) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String timedMessage = String.format("[%s] %s", currentTime, message);

        System.out.println(timedMessage);

        new Thread(() -> {
            try {
                playSound();

                Notify.MOVE_DURATION = 0.45f;
                Notify.MAIN_TEXT_FONT = "Source Code Pro BOLD 14";

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

    public static void showPrompt() {
        System.out.print("> ");
    }

    public static void playSound() throws IOException {
        File f = new File("src/main/resources/ding.wav");
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException ex) {
            throw new IOException(ex);
        }
    }
}
