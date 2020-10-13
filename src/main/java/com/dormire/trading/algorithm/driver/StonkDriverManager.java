package com.dormire.trading.algorithm.driver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StonkDriverManager {

    private BlockingQueue<StonkDriver> drivers;

    public StonkDriverManager() {
        this.drivers = new LinkedBlockingQueue<>();
    }

    private void addNewDriver() {
        new Thread(() -> {
            try {
                drivers.put(new StonkDriver());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public StonkDriver get() throws InterruptedException {
        addNewDriver();
        return drivers.take();
    }

    public void start() {
        addNewDriver();
    }
}
