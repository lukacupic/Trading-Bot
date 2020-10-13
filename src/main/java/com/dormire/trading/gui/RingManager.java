package com.dormire.trading.gui;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.algorithm.driver.StonkDriverManager;
import com.dormire.trading.gui.controllers.ControllerMediator;
import com.dormire.trading.gui.controllers.MainController;
import com.dormire.trading.util.NotifcationUtil;

public class RingManager {

    private StonkTrader trader;
    private MainController mainController;

    public RingManager(Instrument instrument, StonkDriverManager driverManager) {
        this.trader = new StonkTrader(this, driverManager, instrument.getTicker(),
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

    public void showOkAlert(String format, Object... arguments) {
        String message = String.format(format, arguments);
        mainController.showAlert(message, "OK");
    }

    public String showYesNoAlert(String format, Object... arguments) {
        String message = String.format(format, arguments);
        return mainController.showAlert(message, "YES", "NO").toUpperCase();
    }

    public String showInputDialog(String format, Object... arguments) {
        String message = String.format(format, arguments);
        return mainController.showInputDialog(message).toUpperCase();
    }

    public void setMessage(String format, Object... arguments) {
        String message = String.format(format, arguments);
        mainController.updateMainLabel(message);
    }

    public void showNotification(String format, Object... arguments) {
        String message = String.format(format, arguments);
        NotifcationUtil.showNotification(message);
    }

    public void setCurrentStep(int step) {
        mainController.updateStepLabel("STEP " + step);
    }
}
