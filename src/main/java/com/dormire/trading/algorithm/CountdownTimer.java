package com.dormire.trading.algorithm;

import java.util.function.Consumer;

public class CountdownTimer {

    private volatile int seconds;
    private Consumer<Integer> task;

    public CountdownTimer() {
    }

    public CountdownTimer(int seconds, Consumer<Integer> task) {
        this.seconds = seconds;
        this.task = task;
    }

    public void start() throws InterruptedException {
        while (seconds >= 0) {
            task.accept(seconds);
            setTime(seconds - 1);
            StonkTrader.sleep(1);
        }
    }

    public void setTask(Consumer<Integer> task) {
        this.task = task;
    }

    public synchronized void setTime(int seconds) {
        this.seconds = seconds;
    }
}