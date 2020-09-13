package com.dormire.trading.utils;

import java.util.function.BooleanSupplier;

public class RuntimeUtil {

    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignorable) {
        }
    }

    public static void waitConditional(BooleanSupplier condition, int seconds) {
        while (!condition.getAsBoolean()) {
            wait(seconds);
        }
    }
}