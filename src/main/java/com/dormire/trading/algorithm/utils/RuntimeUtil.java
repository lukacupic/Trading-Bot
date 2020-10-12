package com.dormire.trading.algorithm.utils;

import com.dormire.trading.gui.RingManager;
import javafx.application.Platform;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RuntimeUtil {

    private static BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignorable) {
        }
    }

    public static void updateCurrentStep(int value, RingManager manager) throws InterruptedException {
        Platform.runLater(() -> {
            manager.setCurrentStep(value);
            try {
                queue.put("");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        queue.take();
    }
}