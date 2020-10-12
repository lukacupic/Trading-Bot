package com.dormire.trading.gui;

import com.dormire.trading.util.UserInputUtil;

public class Instrument {

    private String ticker;
    private double price;
    private double noStonks;
    private double percentage;

    public Instrument(String ticker, String price, String noStonks, String percentage) {
        this.ticker = UserInputUtil.prepareString(ticker);
        this.price = UserInputUtil.prepareNumber(price);
        this.noStonks = UserInputUtil.prepareNumber(noStonks);
        this.percentage = UserInputUtil.prepareNumber(percentage);
    }

    public String getTicker() {
        return ticker;
    }

    public double getPrice() {
        return price;
    }

    public double getNoStonks() {
        return noStonks;
    }

    public double getPercentage() {
        return percentage;
    }
}