package com.dormire.trading.gui;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.gui.controllers.ControllerManager;
import com.dormire.trading.gui.controllers.MainController;
import com.dormire.trading.util.NotifcationUtil;

public class GuiManager {

    private MainController mainController;
    private StonkTrader activeTrader;

    public GuiManager() {
        this.mainController = ControllerManager.getInstance().getMainController();
    }

    public GuiManager(MainController mainController) {
        this.mainController = mainController;
    }

    public void showOkAlert(String format, Object... arguments) {
        String message = String.format(format, arguments);
        mainController.showAlert(message, "OK");
    }

    public synchronized String showYesNoAlert(String format, Object... arguments) {
        String message = String.format(format, arguments);
        return mainController.showAlert(message, "YES", "NO").toUpperCase();
    }

    public synchronized String showInputDialog(String format, Object... arguments) {
        String message = String.format(format, arguments);
        return mainController.showInputDialog(message).toUpperCase();
    }

    public void showNotification(String format, Object... arguments) {
        String message = String.format(format, arguments);
        NotifcationUtil.showNotification(message);
    }

    public void setStep(int step) {
        if (step == 0) {
            mainController.updateStepLabel("");
        } else {
            mainController.updateStepLabel("STEP " + step);
        }
    }

    public void setMessage(String format, Object... arguments) {
        if (format == null) {
            mainController.updateMainLabel("");
        } else {
            String message = String.format(format, arguments);
            mainController.updateMainLabel(message);
        }
    }

    public void setActiveTrader(StonkTrader trader) {
        this.activeTrader = trader;
    }

    public StonkTrader getActiveTrader() {
        return activeTrader;
    }

    public void refresh() {
        setStep(activeTrader.getStep());
        setMessage(activeTrader.getMessage());
    }
}