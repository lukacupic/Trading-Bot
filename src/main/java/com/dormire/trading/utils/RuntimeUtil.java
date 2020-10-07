package com.dormire.trading.utils;

public class RuntimeUtil {

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ignorable) {
        }
    }
}