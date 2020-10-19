package com.dormire.trading.algorithm;

import java.util.function.Consumer;

public class CountdownTimer {

    private int seconds;
    private Consumer<Integer> task;

    public CountdownTimer(int seconds, Consumer<Integer> task) {
        this.seconds = seconds;
        this.task = task;
    }

    public void start() throws InterruptedException {
        while (seconds >= 0) {
            task.accept(seconds--);
            StonkTrader.sleep(1);
        }
    }
}