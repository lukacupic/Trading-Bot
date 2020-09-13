package com.dormire.trading.utils;

import java.util.function.BooleanSupplier;

public class RuntimeUtil {

    public static void wait(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    public static void waitConditional(BooleanSupplier condition, int seconds) throws InterruptedException {
        while (!condition.getAsBoolean()) {
            wait(seconds);
        }
    }
}
