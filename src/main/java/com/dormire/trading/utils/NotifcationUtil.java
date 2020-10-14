package com.dormire.trading.utils;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotifcationUtil {

    public static void showNotification(String message) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String timedMessage = String.format("[%s] %s", currentTime, message);

        new Thread(() -> {
            try {
                InputStream is = NotifcationUtil.class.getResource("/images/musk.png").openStream();
                playSound();

                Notify.MOVE_DURATION = 0.45f;
                Notify.MAIN_TEXT_FONT = "Source Code Pro BOLD 14";

                Notify.create()
                        .title("Papi Musk")
                        .position(Pos.TOP_LEFT)
                        .text(timedMessage)
                        .setScreen(1)
                        .image(ImageIO.read(is))
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void playSound() throws IOException {
        InputStream is = NotifcationUtil.class.getResource("/sounds/ding.wav").openStream();
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException ex) {
            throw new IOException(ex);
        }
    }
}
