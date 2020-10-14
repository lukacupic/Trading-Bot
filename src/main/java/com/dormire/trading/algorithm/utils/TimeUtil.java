package com.dormire.trading.algorithm.utils;

public class TimeUtil {


    public static String formatTime(int seconds) {
        if (seconds < 60) return String.valueOf(seconds) + " s";

        int min = (int) Math.floor(seconds / 60.0);
        int sec = seconds % 60;

        String minFormat = min < 10 ? "0%d" : "%d";

        return String.format(minFormat + " m %d s", min, sec);
    }
}
