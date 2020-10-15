package com.dormire.trading.gui.controllers;

import com.dormire.trading.algorithm.StonkTrader;
import com.dormire.trading.algorithm.driver.StonkDriverManager;
import com.dormire.trading.gui.instruments.Instrument;
import com.dormire.trading.gui.instruments.InstrumentManager;
import com.dormire.trading.gui.scenes.PromptScene;
import com.dormire.trading.gui.GuiManager;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

public class MainController {

    private static final String TICKER_LOGO_URL = "https://trading212equities.s3.eu-central-1.amazonaws.com/%s.png";

    @FXML
    private JFXButton addButton;

    @FXML
    private VBox instrumentPane;

    @FXML
    private Label stepLabel;

    @FXML
    private Label mainLabel;

    @FXML
    private ImageView ringView;

    private GuiManager guiManager;
    private InstrumentManager instrumentManager;
    private StonkDriverManager driverManager;

    public MainController() {
        ControllerManager.getInstance().setMainController(this);
    }

    public void initialize() {
        this.driverManager = new StonkDriverManager();
        this.guiManager = new GuiManager();
        this.instrumentManager = new InstrumentManager(instrumentPane);

        driverManager.start();

        createHomeButton();

        addButton.setOnAction(event -> {
            try {
                Scene mainScene = ((Node) (event.getSource())).getScene();
                new PromptScene(mainScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        double ringSize = 350;
        ringView.setFitWidth(ringSize);
        ringView.setFitHeight(ringSize);

        bindLabelFontSize(stepLabel, 0.8 * ringSize, 18);
        bindLabelFontSize(mainLabel, 0.8 * ringSize, 18);
    }

    private void createHomeButton() {
        HBox home = loadInstrument();
        home.setOnMouseClicked(event -> {
            instrumentManager.setActive(home);
            guiManager.setActiveTrader(null);
            guiManager.setStep(0);
            guiManager.setMessage("");
        });
        instrumentManager.addInstrument(home);

        ImageView imageView = (ImageView) home.getChildren().get(0);
        InputStream homeStream = getClass().getResourceAsStream("/images/home.png");
        imageView.setImage(new Image(homeStream));
        imageView.setFitWidth(50);
    }

    private void bindLabelFontSize(Label label, double ringSize, double defaultFontSize) {
        Font defaultFont = Font.font(defaultFontSize);

        label.textProperty().addListener((observable, oldValue, newValue) -> {
            Text tempText = new Text(newValue);
            tempText.setFont(defaultFont);

            double textWidth = tempText.getLayoutBounds().getWidth();

            if (textWidth <= ringSize) {
                label.setFont(defaultFont);

            } else {
                double newFontSize = defaultFontSize * ringSize / textWidth;
                label.setFont(Font.font(defaultFont.getFamily(), newFontSize));
            }
        });
    }

    public void addInstrument(Instrument instrument) {
        StonkTrader trader = new StonkTrader(guiManager, driverManager, instrument);
        trader.start();
        ringView.setVisible(true);

        createHBox(instrument, trader);
    }

    private void createHBox(Instrument instrument, StonkTrader trader) {
        HBox instrumentBox = loadInstrument();

        new Thread(() -> {
            // set logo image for the instrument
            String url = String.format(TICKER_LOGO_URL, instrument.getTicker());
            ImageView imageView = (ImageView) instrumentBox.getChildren().get(0);
            imageView.setImage(new Image(url));
        }).start();

        instrumentBox.setOnMouseClicked(event -> {
            instrumentManager.setActive(instrumentBox);
            guiManager.setActiveTrader(trader);
            guiManager.refresh();
        });

        instrumentManager.addInstrument(instrumentBox);

        // TODO: remove instrument mouse-clicked duplicate by delegating the task to the instrument itself
        instrumentManager.setActive(instrumentBox);
        guiManager.setActiveTrader(trader);
        guiManager.refresh();

    }

    private HBox loadInstrument() {
        try {
            return FXMLLoader.load(getClass().getResource("/interfaces/instrument.fxml"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void updateMainLabel(String text) {
        mainLabel.setText(text);
    }

    public void updateStepLabel(String text) {
        stepLabel.setText(text);
    }

    public String showAlert(String text, String... buttons) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(text);

        ButtonType[] buttonTypes = Arrays.stream(buttons).map(ButtonType::new).toArray(ButtonType[]::new);

        alert.getButtonTypes().setAll(buttonTypes);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get().getText();
    }

    public String showInputDialog(String text) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(null);
        dialog.setHeaderText(null);
        dialog.setContentText(text);

        Optional<String> result = dialog.showAndWait();
        return result.get();
    }

    public void shutdown() {
        driverManager.dispose();
    }
}