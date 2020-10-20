package com.dormire.trading.utils;

public class UserInputUtil {

    public static String prepareString(String value) {
        return value.trim().toUpperCase();
    }

    public static double prepareDouble(String value) {
        return Double.parseDouble(value.replaceAll(",", "."));
    }

    public static int prepareInt(String value) {
        return Integer.parseInt(value);
    }
}