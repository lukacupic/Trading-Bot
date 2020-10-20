package com.dormire.trading.gui;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.utils.NotifcationUtil;
import com.dormire.trading.utils.UserInputUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class GUIManager {

    private static GUIManager instance;

    private ImageView ringView;
    private Label mainLabel;
    private Label stepLabel;

    private StonkTrader activeTrader;
    private Map<String, Image> ringImages;

    public GUIManager(ImageView ringView, Label mainLabel, Label stepLabel) {
        GUIManager.instance = this;
        this.ringImages = new HashMap<>();

        this.ringView = ringView;
        this.mainLabel = mainLabel;
        this.stepLabel = stepLabel;
    }

    public synchronized void showOkAlert(String format, Object... arguments) {
        String message = String.format(format, arguments);
        showAlert(message, "OK");
    }

    public synchronized String showYesNoAlert(String format, Object... arguments) {
        String message = String.format(format, arguments);
        return showAlert(message, "YES", "NO").toUpperCase();
    }

    public synchronized double showNumberInputDialog(String format, Object... arguments) {
        String message = String.format(format, arguments);
        return UserInputUtil.prepareDouble(showInputDialog(message));
    }

    public synchronized void showNotification(String format, Object... arguments) {
        String message = String.format(format, arguments);
        NotifcationUtil.showNotification(message);
    }

    public synchronized void updateStep(int step) {
        if (step <= 0) {
            updateStepLabel("");
        } else {
            updateStepLabel("STEP " + step);
        }
    }

    private void updateStepLabel(String text) {
        stepLabel.setText(text);
    }

    public synchronized void updateMessage(String format, Object... arguments) {
        if (format == null) {
            updateMainLabel("");
        } else {
            String message = String.format(format, arguments);
            updateMainLabel(message);
        }
    }

    private void updateMainLabel(String text) {
        mainLabel.setText(text);
    }

    public synchronized void updateActiveTrader(StonkTrader trader) {
        this.activeTrader = trader;
    }

    public synchronized void updateRingColor(RingColor color) {
        String imageName = String.format("/images/ring-%s.png", color.name().toLowerCase());

        // fetch the image from the map, or load it from resources if it doesn't yet exist
        Image image = ringImages.computeIfAbsent(imageName, new Function<>() {
            @Override
            public Image apply(String s) {
                InputStream is = getClass().getResourceAsStream(imageName);
                return new Image(is);
            }
        });

        ringView.setImage(image);
    }

    public synchronized void refresh() {
        if (activeTrader == null) return;
        updateStep(activeTrader.getStep());
        updateMessage(activeTrader.getMessage());
    }

    public StonkTrader getActiveTrader() {
        return activeTrader;
    }

    public static GUIManager getInstance() {
        return instance;
    }

    private static String showAlert(String text, String... buttons) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(text);

        ButtonType[] buttonTypes = Arrays.stream(buttons).map(ButtonType::new).toArray(ButtonType[]::new);

        alert.getButtonTypes().setAll(buttonTypes);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() ? result.get().getText() : "";
    }

    private static String showInputDialog(String text) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setContentText(text);

        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }
}