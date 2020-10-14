package com.dormire.trading.algorithm;

import com.dormire.trading.algorithm.utils.RuntimeUtil;

import java.util.function.Consumer;

public class Timer {

    private int seconds;
    private Consumer<Integer> task;

    public Timer(int seconds, Consumer<Integer> task) {
        this.seconds = seconds;
        this.task = task;
    }

    public void start() {
        while (seconds >= 0) {
            task.accept(seconds--);
            RuntimeUtil.sleep(1);
        }
    }
}
