package com.dormire.trading.gui.instruments;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.utils.UserInputUtil;
import javafx.scene.layout.HBox;

public class Instrument {

    private String ticker;
    private double price;
    private double noStonks;
    private double profit;
    private int loopTime;
    private int waitTime;
    private double bufferZone;
    private double visualBufferZone;

    private HBox box;
    private StonkTrader trader;

    public Instrument(String ticker, String price, String noStonks, String profit, String loopTime, String waitTime,
                      String bufferZone, String visualBufferZone) {
        setTicker(ticker);
        setPrice(price);
        setNoStonks(noStonks);
        setProfit(profit);
        setLoopTime(loopTime);
        setWaitTime(waitTime);
        setBufferZone(bufferZone);
        setVisualBufferZone(visualBufferZone);
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

    public double getProfit() {
        return profit;
    }

    public int getLoopTime() {
        return loopTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public double getBufferZone() {
        return bufferZone;
    }

    public double getVisualBufferZone() {
        return visualBufferZone;
    }

    private void setTicker(String ticker) {
        this.ticker = UserInputUtil.prepareString(ticker);
    }

    public void setPrice(String price) {
        this.price = UserInputUtil.prepareDouble(price);
    }

    public void setNoStonks(String noStonks) {
        this.noStonks = UserInputUtil.prepareDouble(noStonks);
    }

    public void setProfit(String profit) {
        this.profit = UserInputUtil.prepareDouble(profit);
    }

    public void setLoopTime(String loopTime) {
        this.loopTime = UserInputUtil.prepareInt(loopTime);
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = UserInputUtil.prepareInt(waitTime);
    }

    public void setBufferZone(String bufferZone) {
        this.bufferZone = UserInputUtil.prepareDouble(bufferZone);
    }

    public void setVisualBufferZone(String visualBufferZone) {
        this.visualBufferZone = UserInputUtil.prepareDouble(visualBufferZone);
    }

    public HBox getBox() {
        return box;
    }

    public void setBox(HBox box) {
        this.box = box;
    }

    public StonkTrader getTrader() {
        return trader;
    }

    public void setTrader(StonkTrader trader) {
        this.trader = trader;
    }
}