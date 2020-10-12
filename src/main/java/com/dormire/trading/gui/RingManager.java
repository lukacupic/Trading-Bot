package com.dormire.trading.gui;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.gui.controller.ControllerMediator;
import com.dormire.trading.gui.controller.MainController;

public class RingManager {

    private StonkTrader trader;
    private MainController mainController;

    public RingManager(Instrument instrument) {
        this.trader = new StonkTrader(this, instrument.getTicker(),
                instrument.getPrice(), instrument.getPercentage(), instrument.getNoStonks());
        this.mainController = ControllerMediator.getInstance().getMainController();
    }

    /**
     * Starts the RingManager.
     */
    public void start() {
        new Thread(() -> {
            try {
                trader.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Displays an OK alert to the user and returns the user's clicked response.
     * The method guarantees that the provided response will always be uppercase.
     *
     * @param text the text to display to the user
     * @return the user's reply, i.e. "OK"
     */
    public String showOkAlert(String text) {
        return mainController.showAlert(text, "OK").toUpperCase();
    }

    /**
     * Displays the alert to the user and returns the user's clicked response.
     * The method guarantees that the provided response will always be uppercase.
     *
     * @param text the text to display to the user
     * @return the user's reply, i.e. "YES" or "NO"
     */
    public String showYesNoAlert(String text) {
        return mainController.showAlert(text, "YES", "NO").toUpperCase();
    }

    public String showInputDialog(String text) {
        return mainController.showInputDialog(text).toUpperCase();
    }

    public void showMessage(String text) {
        mainController.updateMainLabel(text);
    }

    public void setCurrentStep(int step) {
        mainController.updateStepLabel("STEP " + step);
    }
}
