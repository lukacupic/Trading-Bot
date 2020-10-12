package com.dormire.trading.util;

public class UserInputUtil {

    public static String prepareString(String value) {
        return value.trim().toUpperCase();
    }

    public static double prepareNumber(String value) {
        return Double.parseDouble(value.replaceAll(",", "."));
    }
}
