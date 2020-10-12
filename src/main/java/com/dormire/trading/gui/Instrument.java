package com.dormire.trading.gui;

public class Instrument {

    private String ticker;
    private double price;
    private double noStonks;
    private double percentage;

    public Instrument(String ticker, String price, String noStonks, String percentage) {
        this.ticker = ticker.toUpperCase();
        this.price = Double.parseDouble(price);
        this.noStonks = Double.parseDouble(noStonks);
        this.percentage = Double.parseDouble(percentage);
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