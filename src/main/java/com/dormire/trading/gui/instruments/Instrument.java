package com.dormire.trading.gui.instruments;

import com.dormire.trading.utils.UserInputUtil;
import javafx.scene.layout.HBox;

public class Instrument {

    private String ticker;
    private double price;
    private double noStonks;
    private double percentage;
    private HBox box;

    public Instrument(String ticker, String price, String noStonks, String percentage) {
        this.ticker = UserInputUtil.prepareString(ticker);
        this.price = UserInputUtil.prepareNumber(price);
        this.noStonks = UserInputUtil.prepareNumber(noStonks);
        this.percentage = UserInputUtil.prepareNumber(percentage);
    }

    public Instrument(HBox box) {
        this.box = box;
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

    public HBox getBox() {
        return box;
    }

    public void setBox(HBox box) {
        this.box = box;
    }

}