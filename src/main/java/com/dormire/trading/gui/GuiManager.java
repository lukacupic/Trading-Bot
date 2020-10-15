package com.dormire.trading.gui;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.gui.controllers.ControllerManager;
import com.dormire.trading.gui.controllers.MainController;
import com.dormire.trading.gui.controllers.RingColor;
import com.dormire.trading.utils.NotifcationUtil;
import com.dormire.trading.utils.UserInputUtil;

public class GuiManager {

    private MainController mainController;
    private StonkTrader activeTrader;

    public GuiManager() {
        this.mainController = ControllerManager.getInstance().getMainController();
    }

    public synchronized void showOkAlert(String format, Object... arguments) {
        String message = String.format(format, arguments);
        mainController.showAlert(message, "OK");
    }

    public synchronized String showYesNoAlert(String format, Object... arguments) {
        String message = String.format(format, arguments);
        return mainController.showAlert(message, "YES", "NO").toUpperCase();
    }

    public synchronized double showNumberInputDialog(String format, Object... arguments) {
        String message = String.format(format, arguments);
        return UserInputUtil.prepareNumber(mainController.showInputDialog(message));
    }

    public synchronized void showNotification(String format, Object... arguments) {
        String message = String.format(format, arguments);
        NotifcationUtil.showNotification(message);
    }

    public synchronized void setStep(int step) {
        if (step == 0) {
            mainController.updateStepLabel("");
        } else {
            mainController.updateStepLabel("STEP " + step);
        }
    }

    public synchronized void setMessage(String format, Object... arguments) {
        if (format == null) {
            mainController.updateMainLabel("");
        } else {
            String message = String.format(format, arguments);
            mainController.updateMainLabel(message);
        }
    }

    public synchronized void updateRingColor(RingColor color) {
        mainController.updateRingColor(color);
    }

    public synchronized void refresh() {
        if (activeTrader == null) return;
        setStep(activeTrader.getStep());
        setMessage(activeTrader.getMessage());
    }

    public synchronized void setActiveTrader(StonkTrader trader) {
        this.activeTrader = trader;
    }

    public StonkTrader getActiveTrader() {
        return activeTrader;
    }
}